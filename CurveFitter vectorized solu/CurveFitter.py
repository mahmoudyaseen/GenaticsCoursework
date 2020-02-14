# -*- coding: utf-8 -*-
"""
Created on Fri Nov  1 16:37:40 2019

@author: MahmoudYassen
"""

import numpy as np
import matplotlib.pyplot  as plt

def fitnessFunction(individuals , X , Y):
    '''
    take population , X , Y
    and return error and predicted Y for each chromosome
    '''
    #individuals is array of shape(n , d)
    #each row is chromosome
    #n is the number of the chromosome
    #d is the degree of equation
    #
    #X the training set of shape (m , d)
    #each row is point
    #m the number of points
    #d is the degree of equation
    #
    #Y is array of shape (m , 1) the correct output
    #m the number of points
    #
    #YPredict array of shape (m , n)
    #each column is Ypredicted for one chromosome
    #
    #(m , d) * (d , n) = (m , n)
    YPredict = np.dot(X , individuals.T)
    #m the numper of points
    m = Y.shape[0]
    #error array of shape(1 , n)
    #each column is mean square error of one chromosome
    error = np.sum(np.square(np.subtract(Y,YPredict)) , axis=0 , keepdims=True) / m
    #return error for all population and the predicted value of Y
    return error , YPredict

def generateRandomIndividuals(rangeMin , rangeMax , numberOfPop , degree):
    '''
    generate population return array of shape (number of population , degree of equations)
    '''
    #generate random individuals of shape (n , d)
    return np.random.uniform(low=rangeMin, high=rangeMax, size=(numberOfPop , degree+1))


def mutation(individuals , rangeMin , rangeMax , t , T , b):
    '''
    make non-uniform mutation for all individuals
    take old population , min , max , current iteration , total number of iteration , b
    and return new mutated population
    '''
    #individuals array of shape(n , d)
    #t the current generation
    #T total number of generations
    #b parameter
    #
    #delta(U/L) is array of shape(n,d)
    #each row is one chromosome
    #n is the number of population
    #d the degree of equation
    #deltaU is the difrence of max value of coefficient and the current coefficient
    #deltaL if the difrence of coefficient and the min value of coefficient
    #
    deltaU = rangeMax - individuals
    deltaL = individuals - rangeMin
    #make array of random with the same shape of individuals
    r1 =  np.random.uniform(size=individuals.shape)
    #bigger is array of shape of r1
    #used to know which value > 0.5 
    bigger = r1 > 0.5
    #y1 is array of shape of individuals 
    #if r1[i][j] > 0.5 , y1[i][j] = deltaU , else y[i][j] = 0
    y1 = bigger * deltaU
    #smalle is array of shape of r1
    #used to know which value <= 0.5 
    smaller = r1 <= 0.5
    #y2 is array of shape of individuals 
    #if r2[i][j] <= 0.5 , y2[i][j] = deltal , else y[i][j] = 0
    y2 = smaller * deltaL
    #put y1 , y2 in y
    #if r1[i][j] > 0.5 , y[i][j] = deltaU[i][j] else y[i][j] = deltaL[i][j]
    y = y1 + y2
    #rt is random numbers of shape of individuals
    rt = np.random.uniform(size=individuals.shape)
    #calculate delta with non-uniform mutaion method
    delta = y * (1 - rt ** ((1 - t / T) ** b))
    #r2 is random array of shape of individuals it's values from 0 to delta
    r2 = np.random.uniform(high=delta, size=None)
    #if r1[i][j] > 0.5 , coefficient[i][j] = coefficient[i][j] + r2[i][j]
    individuals = individuals + bigger * r2
    #if r1[i][j] < 0.5 , coefficient[i][j] = coefficient[i][j] - r2[i][j]
    individuals = individuals - smaller * r2
    #return updated individuals
    return individuals

def process(numOfGeneration , numberOfPop , degree , XPoint , Y ):
    '''
    make optimization using genatic algorithms 
    take number of generations , number of populations , degree of equation , points(training set)[X,Y]
    return the best chromosome fits the dataset, it's error and it's Yprediction
    '''
    #rangeMin is the minimum value of coefficient
    rangeMin = -10
    #rangeMax is the maximum value of coefficient
    rangeMax =  10
    #hyperparameters used in mutaion
    b = 5
    #number of points(training set)
    m = Y.shape[0]
    #generate random population
    individuals = generateRandomIndividuals(rangeMin , rangeMax , numberOfPop , degree)
    #X is array of shape(m , d)
    #m is the number of points
    #d the degree of equation
    #each row is input of equation [a0*x1^0,a1*x1^1,a1*x1^2,...]
    #add first colum = 1 -> x^0
    X0 = np.ones((m,1))
    X = np.c_[X0,XPoint]
    #add rest of columns x^2 , x^3 , ... -> depend on degree of your equation
    for i in range(degree-1):
        X = np.c_[X , XPoint**(i+2)]
    #loop for number of generation
    for i in range(numOfGeneration):
        #mutate populations
        individuals = mutation(individuals , rangeMin , rangeMax , i , numOfGeneration , b)
        #get fitness (errors) of each chromosome
        fit = fitnessFunction(individuals , X , Y)[0]
        #get min erroe
        minError = np.amin(fit)
        #get indexes of min values
        temp = np.where(fit==minError)
        #get best individual
        bestIndividual = individuals[temp[1][0]]
        #make it with correct dimentions
        bestIndividual = bestIndividual.reshape((1,degree+1))
        
        #replace all bad chromosomes (error > mean error) with best solution
        index = fit > fit.mean()
        index = index.reshape((numberOfPop))
        individuals[index] = bestIndividual
        #or replace the worst chromosome with the best chromosome
        #individuals[np.where(fit==np.amax(fit))[1][0]] = bestIndividual
        
    #get fitness and Ypredicted of populations
    fitout = fitnessFunction(individuals , X , Y)
    #fitness (errors)
    fit = fitout[0]
    #get min error
    minError = np.amin(fit)
    temp = np.where(fit==minError)
    #get min chromosome
    bestIndividual = individuals[temp[1][0]]
    #dit Ypredict of min chromosome
    YPredict = (fitout[1])[:,temp[1][0]]
    #return best chromosome , his error , his Yprediction
    return bestIndividual , minError , YPredict

def predict(individual , XPoint , Y):
    '''
    take chromosome and points(test set)[X , Y] and return predicted Y and Error
    '''
    #individuals is array of shape(m,)
    #XPoint,Y is array of shape (m,1)
    
    #calculate degree
    degree = individual.shape[0]-1
    #calculate numper of test set
    m = XPoint.shape[0]
    #make X of shape (number of test set , degree of equation)
    X0 = np.ones((m,1))
    X = np.c_[X0,XPoint]
    for i in range(degree-1):
        X = np.c_[X , XPoint**(i+2)]
    
    #predict Ypredicate
    YPredict = X.dot(individual)
    #make Ypredict with correct shape
    YPredict = YPredict.reshape((m , 1))
    #calclate error
    error = np.sum(np.square(np.subtract(Y,YPredict)) , axis=0 , keepdims=True) / m
    return YPredict , error

#take input from file
f = open("input-2.txt", "r")
lines = f.read().splitlines()
numOfPoints = 0
numberOfTestcases = int(lines[0])
for i in range(numberOfTestcases):
    numOfPoints = int(lines[i * (numOfPoints+1) + 1].split()[0])
    degree = int(lines[i * (numOfPoints+1) + 1].split()[1])
    XPoint = []
    Y = []
    for j in range(numOfPoints):
        XPoint.append(float(lines[i * (numOfPoints+1) + 1 + j + 1].split()[0]))
        Y.append(float(lines[i * (numOfPoints+1) + 1 + j + 1].split()[1]))
    #reshape XPoint , Y to correct dimentions
    XPoint = np.array(XPoint).reshape((numOfPoints , 1))
    Y = np.array(Y).reshape((numOfPoints , 1))
    #start optimization
    out = process(numOfGeneration=200000 , numberOfPop=100 , degree=degree , XPoint=XPoint , Y=Y )
    bestChromosome = out[0]
    bestError = out[1]
    Ypredicted = out[2]
    print(bestChromosome)
    print(bestError)
    
    #using fun predict
    #Predicted = predict(out[0] , XPoint , Y)
    #Ypredicted = Predicted[0]
    #Error = Predicted[1]
    #test on correct output...
    #print(predict(np.array([0.417839,0.875352,-0.0891128,-0.269502,0.0754247,-0.00538435]) , XPoint , Y)[1])
    
    #plot
    plt.scatter(XPoint, Y, color = 'red')
    plt.plot(XPoint, Ypredicted , color = 'blue')
    plt.show()
f.close()    

