%%This script plots the values of the well mixed game
%with N strategies. For b-values from 1 to 5.
%The game model used updates asynchronously.
%The second data set contains values obtained
%through application of strategic groups.
%2d

clear
close all
clc
hold on

%population sizes
pop_sizes = [10, 40, 100, 128, 256, 512 , 1024];

%read files for well mixed game with N-1 strategies
a = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 10.0.csv');
b = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 40.0.csv');
c = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 100.0.csv');
d = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 128.0.csv');
e = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 256.0.csv');
f = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 512.0.csv');
g = csvread('CBR_ 3.0_Structured_2D_Multi_PopulationSize_ 1024.0.csv');
multi = [a(end),b(end),c(end),d(end),e(end),f(end),g(end)];
plot(pop_sizes, multi,'^-');

h = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 10.0.csv');
i = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 40.0.csv');
j = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 100.0.csv');
k = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 128.0.csv');
l = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 256.0.csv');
m = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 512.0.csv');
n = csvread('CBR_ 3.0_Structured_2D_Single_PopulationSize_ 1024.0.csv');
single = [h(end), i(end), j(end), k(end), l(end), m(end), n(end)];

a1 = csvread('1D Structured - Two Strategies with10.0_Nodes.csv');
b1 = csvread('1D Structured - Two Strategies with40.0_Nodes.csv');
c1 = csvread('1D Structured - Two Strategies with100.0_Nodes.csv');
d1 = csvread('1D Structured - Two Strategies with128.0_Nodes.csv');
e1 = csvread('1D Structured - Two Strategies with256.0_Nodes.csv');
f1 = csvread('1D Structured - Two Strategies with512.0_Nodes.csv');
g1 = csvread('1D Structured - Two Strategies with1024.0_Nodes.csv');
multi_1D = [a1(end),b1(end),c1(end),d1(end),e1(end),f1(end),g1(end)];
plot(pop_sizes, single,'o-');
plot(pop_sizes, multi_1D,'<-');

a2 = csvread('SingleStrategy_Ring_Avgs10.0PD.csv');
b2 = csvread('SingleStrategy_Ring_Avgs40.0PD.csv');
c2 = csvread('SingleStrategy_Ring_Avgs100.0PD.csv');
d2 = csvread('SingleStrategy_Ring_Avgs128.0PD.csv');
e2 = csvread('SingleStrategy_Ring_Avgs256.0PD.csv');
f2 = csvread('SingleStrategy_Ring_Avgs512.0PD.csv');
g2 = csvread('SingleStrategy_Ring_Avgs1024.0PD.csv');
single_1D = [a2(end),b2(end),c2(end),d2(end),e2(end),f2(end),g2(end)];
plot(pop_sizes, single_1D, '>-');
xlabel('Population Size');
ylabel('fraction of cooperation');

legend('Model C: 2D', 'Model B: 2D', 'Model C: 1D', 'Model B: 1D');

title('Performance in structured population, b = 3.0');