# -*- coding: utf-8 -*-
"""
Created on Tue Dec 24 19:29:33 2019

@author: MahmoudYassen
"""

import numpy as np

def read_file(file_name="train.txt"): 
    file = open(file_name, "r")
    input_nodes, hidden_nodes, output_nodes  = np.array((file.readline()).split(" ")).astype('int')
    num_training_ex = int((file.readline()))
    data_set = file.read().splitlines()
    data_set = [line.split() for line in data_set]
    data_set = np.array(data_set).astype('float32')
    file.close()
    return input_nodes, hidden_nodes, output_nodes, num_training_ex, data_set

def normalize(X):
    mean = X.mean(axis=0)
    min = X.min(axis=0)
    max = X.max(axis=0)
    Xnew = (X - mean) / (max - min)
    return Xnew

def sigmoid(Z):
    return 1/(1+ np.exp(-Z))

def feed_forward(X, Y, W1, b1, W2, b2, num_training_ex):
    #fist layer
    #W1(hidden,input-nodes)*X.T(input-nodes, num-of-ex) = Z1(hidden,input-ex)
    #batch-size = all-training-ex -> (10,8)*(8,515)=(10,515)
    #batch-size = 1               -> (10,8)*(8,1)=(10,1)
    Z1 = np.dot(W1 , X.T) + b1
    A1 = sigmoid(Z1)
    #second layer
    #W2(output,hidden)*A1(hidden,input-ex) = Z2(output,input-ex)
    #batch-size = all-training-ex -> (1,10)*(10,515)=(1,515)
    #batch-size = 1               -> (1,10)*(10,1)=(1,1)
    Z2 = np.dot(W2 , A1) + b2
    #final layer is real values so we will use normal regression
    A2 = Z2
    MSE = np.sum(np.square(Y.T - A2)) /  num_training_ex
    return A1 , A2 , MSE

def back_propagation(X, Y, W1, b1, W2, b2, batch_size, number_of_epoch):
    number_training = X.shape[0]
    learning_rate = 0.01
    #loop in epochs
    for i in range(1,number_of_epoch+1):
        #loop on batches
        for j in range(0, number_training, batch_size):
            X_train = X[j:j + batch_size,:]
            Y_train = Y[j:j + batch_size,:]
            A1 , A2 , MSE = feed_forward(X_train, Y_train, W1, b1, W2, b2, batch_size)
            #E2(output,input-ex)
            #batch = all-ex -> (1,515) , batch = 1 -> (1,1)
            #becouse it not sigmoid
            E2 = Y_train.T - A2
            sigmoid_dif = np.multiply(A1 , 1-A1)
            #w2.T(hidden,output)*E2(output,input-ex) = E1(hidden , input-ex)
            #(10,515)   -  (10,1)
            E1 = np.multiply(np.dot(W2.T,E2),sigmoid_dif)#
            #E2(output,input-ex)*A1.T(input-ex,hidden) = (output,hidden)
            #E2(1,515)*A1.T(515,10) = (1,10)
            #E2(1,1)*A1.T(1,10) = (1,10)
            W2 = W2 + learning_rate*(np.dot(E2,A1.T)/batch_size)#/batch_size means take avg error of all input_ex
            #(output,1)
            b2 = b2 + learning_rate*(np.sum(E2,axis=1,keepdims=True)/batch_size)
            #E1(hidden,input-ex)*X(input-ex,input-nodes) = (hidden,input-nodes)
            #E1(10,515)*X(515,8) = (10,8)
            #E1(10,1)*X(1,8) = (10,8)
            W1 = W1 + learning_rate*(np.dot(E1,X_train)/batch_size)
            #(hidden,1)
            b1 = b1 + learning_rate*(np.sum(E1,axis=1,keepdims=True)/batch_size)
        if i % 100 == 0:
            A1 , A2 , MSE = feed_forward(X, Y, W1, b1, W2, b2, number_training)
            print('MSE in epoch number ' , i , ' = ' , MSE)
    return W1, b1, W2, b2
        
def model():
    input_nodes, hidden_nodes, output_nodes, num_training_ex, data_set = read_file()
    
    X = data_set[: , :-output_nodes]
    Y = data_set[: , -output_nodes:]
    
    X = normalize(X)
    
    #W1 is 2D_matrix of shape (hidden_nodes , input_nodes)
    #(10,8)
    W1 = np.random.randn(hidden_nodes , input_nodes) * 0.01
    b1 = np.ones((hidden_nodes,1))
    #W2 is 2D_matrix of shape (output_nodes , hidden_nodes)
    #(1*10)
    W2 = np.random.randn(output_nodes , hidden_nodes) * 0.01
    b2 = np.ones((output_nodes,1))
    
    A1 , A2 , MSE = feed_forward(X, Y, W1, b1, W2, b2, num_training_ex)
    print('MSE before training = ', MSE)
    
    W1, b1, W2, b2 = back_propagation(X, Y, W1, b1, W2, b2, batch_size=10, number_of_epoch=1000)
    
    weights = {'W1': W1, 'b1': b1, 'W2': W2, 'b2': b2}
    f = open("weights.txt","w")
    f.write( str(weights) )
    f.close()

model()







