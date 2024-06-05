import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import tensorflow as tf
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.preprocessing import MinMaxScaler, StandardScaler
from sklearn.decomposition import PCA
from scipy import stats
import argparse
import warnings



def outlier_correlation(data, method):
    if (method == 1): #3-sigma
        up = data.mean() + 3 * data.std()
        low = data.mean() - 3 * data.std()
        bool_index = (data < up) & (data > low)
        return data.loc[bool_index,:]
    if (method == 2): #IQR
        m = 1.5
        data = np.array(data)
        p25 = np.percentile(data, 25)
        p75 = np.percentile(data, 75)
        iqr = p75 - p25
        min_val = p25 - (m * iqr)
        max_val = p75 + (m * iqr)
        return data[~((data < min_val) | (data > max_val)).any(axis=1)]

def normalization(data, method=1):

    if(method == 1): #minmax
        scaler = MinMaxScaler()
    if(method == 2): #std
        scaler = StandardScaler()
    scaled_data = pd.DataFrame(scaler.fit_transform(data), columns=data.columns)
    return scaled_data

def dataPCA(data, n):
    pca = PCA(n_components = n)
    pca_data = pca.fit_transform(data)
    return pca_data

def KStest(data, sig_level=0.05):
    mean = data.mean()
    std = data.std()
    pvalue = stats.kstest(data, 'norm', (mean, std))[1]
    return 1 if pvalue > sig_level else 0

def prediction(X_train, Y_train, X_test, method):
    if (method == 1): #RFR
        model = RandomForestRegressor()
    if (method == 2): #XGB
        model = GradientBoostingRegressor()
    model.fit(X_train, Y_train)
    return model.predict(X_test)

def getdata(excelname, usecols, srow, erow):
    df = pd.read_excel('G:\\luckysheet-import-team-edit-export-master\\src\\main\\java\\com\\mars\\ecsheet\\exceldata\\'+excelname, usecols=usecols, header=None)
    data = df.iloc[int(srow)-1:int(erow), :]
    data = data.astype('float32')
    return data

if __name__ == "__main__":
    # 初始化参数构造器
    parser = argparse.ArgumentParser()

    parser.add_argument('--usecols', type=str)
    parser.add_argument('--srow', type=str)
    parser.add_argument('--erow', type=str)
    parser.add_argument('--excelname', type=str)

    # 获取所有的命令行参数
    args = parser.parse_args()

    print(normalization(getdata(args.excelname,args.usecols,args.srow,args.erow)))
