%%This script plots the values of the well mixed game
%with N strategies. For b-values from 1 to 5.
%The game model used updates asynchronously.
%The second data set contains values obtained
%through application of strategic groups.
%
clear
close all
clc
hold on

%17 steps from 1 to 5 at 0.25 increments
cbr = linspace(1,5,17);

%2D structured, dynamic strategies
a = csvread('CBR_ 1.0_Structured_2D_Multi_PopulationSize_ 128.0.csv');
b = csvread('CBR_ 1.25_Structured_2D_Multi_PopulationSize_ 128.0.csv');
c = csvread('CBR_ 1.5_Structured_2D_Multi_PopulationSize_ 128.0.csv');
d = csvread('CBR_ 1.75_Structured_2D_Multi_PopulationSize_ 128.0.csv');
e = csvread('CBR_ 2.0_Structured_2D_Multi_PopulationSize_ 128.0.csv');
f = csvread('CBR_ 2.25_Structured_2D_Multi_PopulationSize_ 128.0.csv');
g = csvread('CBR_ 2.5_Structured_2D_Multi_PopulationSize_ 128.0.csv');
h = csvread('CBR_ 2.75_Structured_2D_Multi_PopulationSize_ 128.0.csv');
i = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 128.0.csv');
j = csvread('CBR_ 3.25_Structured_2D_Multi_PopulationSize_ 128.0.csv');
k = csvread('CBR_ 3.5_Structured_2D_Multi_PopulationSize_ 128.0.csv');
l = csvread('CBR_ 3.75_Structured_2D_Multi_PopulationSize_ 128.0.csv');
m = csvread('CBR_ 4.0_Structured_2D_Multi_PopulationSize_ 128.0.csv');
n = csvread('CBR_ 4.25_Structured_2D_Multi_PopulationSize_ 128.0.csv');
o = csvread('CBR_ 4.5_Structured_2D_Multi_PopulationSize_ 128.0.csv');
p = csvread('CBR_ 4.75_Structured_2D_Multi_PopulationSize_ 128.0.csv');
q = csvread('CBR_ 5.0_Structured_2D_Multi_PopulationSize_ 128.0.csv');
multi_2D = [a(end),b(end),c(end),d(end),e(end),f(end)...
,g(end),h(end),i(end),j(end),k(end),l(end),m(end),n(end),o(end),p(end),q(end)];

plot(cbr, multi_2D,'*-');

%1D structured, dynamic strategies
multi_1D = csvread('CBR for 1D Structured - Two Strategies with128.0_Nodes.csv');
plot(cbr,multi_1D, '+-');

%2D Structure, single strategy
a1 = csvread('CBR_ 1.0_Structured_2D_Single_PopulationSize_ 128.0.csv');
b1 = csvread('CBR_ 1.25_Structured_2D_Single_PopulationSize_ 128.0.csv');
c1 = csvread('CBR_ 1.5_Structured_2D_Single_PopulationSize_ 128.0.csv');
d1 = csvread('CBR_ 1.75_Structured_2D_Single_PopulationSize_ 128.0.csv');
e1 = csvread('CBR_ 2.0_Structured_2D_Single_PopulationSize_ 128.0.csv');
f1 = csvread('CBR_ 2.25_Structured_2D_Single_PopulationSize_ 128.0.csv');
g1 = csvread('CBR_ 2.5_Structured_2D_Single_PopulationSize_ 128.0.csv');
h1 = csvread('CBR_ 2.75_Structured_2D_Single_PopulationSize_ 128.0.csv');
i1 = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 128.0.csv');
j1 = csvread('CBR_ 3.25_Structured_2D_Single_PopulationSize_ 128.0.csv');
k1 = csvread('CBR_ 3.5_Structured_2D_Single_PopulationSize_ 128.0.csv');
l1 = csvread('CBR_ 3.75_Structured_2D_Single_PopulationSize_ 128.0.csv');
m1 = csvread('CBR_ 4.0_Structured_2D_Single_PopulationSize_ 128.0.csv');
n1 = csvread('CBR_ 4.25_Structured_2D_Single_PopulationSize_ 128.0.csv');
o1 = csvread('CBR_ 4.5_Structured_2D_Single_PopulationSize_ 128.0.csv');
p1 = csvread('CBR_ 4.75_Structured_2D_Single_PopulationSize_ 128.0.csv');
q1 = csvread('CBR_ 5.0_Structured_2D_Single_PopulationSize_ 128.0.csv');
single_2D = [a1(end),b1(end),c1(end),d1(end),e1(end),f1(end)...
,g1(end),h1(end),i1(end),j1(end),k1(end),l1(end),m1(end),n1(end),o1(end),p1(end),q1(end)];

%plot
plot(cbr, single_2D, '^-');
single_1D = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
plot(cbr,single_1D,'^-');



%set axis
axis([1, 5, 0 , 0.6]);
%set label
xlabel('b (defection potential)');
ylabel('fraction of cooperation');

legend('Model C: 2D', 'Model C: 1D', 'Model B: 2D', 'Model B: 1D' );

title('b-variance in structured populations Size: 128');