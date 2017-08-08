%%This script plots the values of the well mixed game
%with N strategies. For b-values from 1 to 5.
%The game model used updates asynchronously.
%The second data set contains values obtained
%through application of strategic groups.
%
clear
close all
clc
%read files for well mixed game with N-1 strategies
a = csvread('PD_Well_Mixed_Means_b_Val_1.0_Pop_128.0.csv');
b = csvread('PD_Well_Mixed_Means_b_Val_1.25_Pop_128.0.csv');
c = csvread('PD_Well_Mixed_Means_b_Val_1.5_Pop_128.0.csv');
d = csvread('PD_Well_Mixed_Means_b_Val_1.75_Pop_128.0.csv');
e = csvread('PD_Well_Mixed_Means_b_Val_2.0_Pop_128.0.csv');
f = csvread('PD_Well_Mixed_Means_b_Val_2.25_Pop_128.0.csv');
g = csvread('PD_Well_Mixed_Means_b_Val_2.5_Pop_128.0.csv');
h = csvread('PD_Well_Mixed_Means_b_Val_2.75_Pop_128.0.csv');
i = csvread('PD_Well_Mixed_Means_b_Val_3.0_Pop_128.0.csv');
j = csvread('PD_Well_Mixed_Means_b_Val_3.25_Pop_128.0.csv');
k = csvread('PD_Well_Mixed_Means_b_Val_3.5_Pop_128.0.csv');
l = csvread('PD_Well_Mixed_Means_b_Val_3.75_Pop_128.0.csv');
m = csvread('PD_Well_Mixed_Means_b_Val_4.0_Pop_128.0.csv');
n = csvread('PD_Well_Mixed_Means_b_Val_4.25_Pop_128.0.csv');
o = csvread('PD_Well_Mixed_Means_b_Val_4.5_Pop_128.0.csv');
p = csvread('PD_Well_Mixed_Means_b_Val_4.75_Pop_128.0.csv');
q = csvread('PD_Well_Mixed_Means_b_Val_5.0_Pop_128.0.csv');
%17 steps from 1 to 5 at 0.25 increments
cbr = linspace(1,5,17);

%get final values
a = a(end);
b = b(end);
c = c(end);
d = d(end);
e = e(end);
f = f(end);
g = g(end);
h = h(end);
i = i(end);
j = j(end);
k = k(end);
l = l(end);
m = m(end);
n = n(end);
o = o(end);
p = p(end);
q = q(end);

%read values for group game n/2
%a1
%b1

%extract final values

%get values for single strategy well mixed game
%add final values to vector
pd_well_mixed_final_values = [a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q];
%plot vector
plot(cbr, pd_well_mixed_final_values,'^-');
%set axis
axis([1, 5, 0 , 0.6]);
%set label
xlabel('b (defection potential)');