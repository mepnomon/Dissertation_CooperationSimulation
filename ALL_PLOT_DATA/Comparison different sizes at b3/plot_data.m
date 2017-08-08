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

%population sizes
pop_sizes = [128, 256, 512];

%read files for well mixed game with N-1 strategies
a = csvread('Group_Game_Nodes_128.0_Groups_64_b_3.0.csv');
b = csvread('Group_Game_nodes_256.0_Groups_128_b_3.0.csv');
c = csvread('Group_Game_nodes_512.0_Groups_256_b_3.0.csv');

%get final value
a = a(end);
b = b(end);
c = c(end);
%put the variables into the right sequence
groups_N_2 = [a, b, c];
plot(pop_sizes, groups_N_2,'^-');

e = csvread('Group_Game_Nodes_128.0_Groups_32_b_3.0.csv');
f = csvread('Group_Game_Nodes_256.0_Groups_64_b_3.0.csv');
g = csvread('Group_Game_Nodes_512.0_Groups_128_b_3.0.csv');
e = e(end);
f = f(end);
g = g(end);
groups_n_4 = [e, f, g];
plot(pop_sizes, groups_n_4,'<-');
%get 512

h = csvread('Group_Game_Nodes_128.0_Groups_16_b_3.0.csv');
i = csvread('Group_Game_Nodes_256.0_Groups_32_b_3.0.csv');
j = csvread('Group_Game_Nodes_512.0_Groups_64_b_3.0.csv');
h = h(end);
i = i(end);
j = j(end);
groups_n_8 = [h, i, j];
plot(pop_sizes, groups_n_8,'*--');

%well mixed population (N-1 strategies)
k = csvread('PD_Well_Mixed_Means_128.0.csv');
l = csvread('PD_Well_Mixed_Means_256.0.csv');
m = csvread('PD_Well_Mixed_Means_512.0.csv');
k = k(end);
l = l(end);
m = m(end);
%plot vectors
n_groups = [k, l, m];
plot(pop_sizes, n_groups,'d-');

%single strategy game ring
n = csvread('SingleStrategy_ring_Avgs128.0PD.csv');
o = csvread('SingleStrategy_ring_Avgs256.0PD.csv');
p = csvread('SingleStrategy_ring_Avgs512.0PD.csv');
n = n(end);
o = o(end);
p = p(end);
single = [n,o,p];
plot(pop_sizes, single,'o-');
%set axis
axis([128, 512, 0 , 0.6]);
%set label

%2 strategies
r = csvread('Group_Game_Nodes_128.0_Groups_2_b_3.0.csv');
s = csvread('Group_Game_Nodes_256.0_Groups_2_b_3.0.csv');
t = csvread('Group_Game_Nodes_512.0_Groups_2_b_3.0.csv');
r = r(end);
s = s(end);
t = t(end);
groups_2 = [r,s,t];
plot(pop_sizes, groups_2,'p-');
xlabel('Population Size');
ylabel('fraction of cooperation');

legend('N/2 strategies','N/4 Strategies', 'N/8 Strategies', 'N-1 Strategies' ...
    ,'Single Strategy', '2 Strategies');

title('Performance at different scales, b = 3.0');