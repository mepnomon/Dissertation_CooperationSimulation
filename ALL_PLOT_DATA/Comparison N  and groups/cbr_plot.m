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

%get final values
a = a(end); %1
b = b(end);
c = c(end);
d = d(end);
e = e(end); %2
f = f(end);
g = g(end);
h = h(end);
i = i(end); %3
j = j(end);
k = k(end);
l = l(end);
m = m(end); %4
n = n(end);
o = o(end);
p = p(end);
q = q(end); %5

%read values for group game n/2
a1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_1.0.csv');
b1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_1.25.csv');
c1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_1.5.csv');
d1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_1.75.csv');
e1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_2.0.csv');
f1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_2.25.csv');
g1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_2.5.csv');
h1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_2.75.csv');
i1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_3.0.csv');
j1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_3.25.csv');
k1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_3.5.csv');
l1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_3.75.csv');
m1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_4.0.csv');
n1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_4.25.csv');
o1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_4.5.csv');
p1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_4.75.csv');
q1 = csvread('Group_Game_Nodes_128.0_Groups_64_b_5.0.csv');

%extract final values
a1 = a1(end);
b1 = b1(end);
c1 = c1(end);
d1 = d1(end);
e1 = e1(end);
f1 = f1(end);
g1 = g1(end);
h1 = h1(end);
i1 = i1(end);
j1 = j1(end);
k1 = k1(end);
l1 = l1(end);
m1 = m1(end);
n1 = n1(end);
o1 = o1(end);
p1 = p1(end);
q1 = q1(end);

%read values for group game n/4
a2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_1.0.csv');
b2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_1.25.csv');
c2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_1.5.csv');
d2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_1.75.csv');
e2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_2.0.csv');
f2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_2.25.csv');
g2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_2.5.csv');
h2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_2.75.csv');
i2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_3.0.csv');
j2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_3.25.csv');
k2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_3.5.csv');
l2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_3.75.csv');
m2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_4.0.csv');
n2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_4.25.csv');
o2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_4.5.csv');
p2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_4.75.csv');
q2 = csvread('Group_Game_Nodes_128.0_Groups_32_b_5.0.csv');

%extract final values
a2 = a2(end);
b2 = b2(end);
c2 = c2(end);
d2 = d2(end);
e2 = e2(end);
f2 = f2(end);
g2 = g2(end);
h2 = h2(end);
i2 = i2(end);
j2 = j2(end);
k2 = k2(end);
l2 = l2(end);
m2 = m2(end);
n2 = n2(end);
o2 = o2(end);
p2 = p2(end);
q2 = q2(end);

%read values for group game n/8
a3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_1.0.csv');
b3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_1.25.csv');
c3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_1.5.csv');
d3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_1.75.csv');
e3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_2.0.csv');
f3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_2.25.csv');
g3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_2.5.csv');
h3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_2.75.csv');
i3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_3.0.csv');
j3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_3.25.csv');
k3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_3.5.csv');
l3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_3.75.csv');
m3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_4.0.csv');
n3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_4.25.csv');
o3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_4.5.csv');
p3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_4.75.csv');
q3 = csvread('Group_Game_Nodes_128.0_Groups_16_b_5.0.csv');

%extract final values
a3 = a3(end);
b3 = b3(end);
c3 = c3(end);
d3 = d3(end);
e3 = e3(end);
f3 = f3(end);
g3 = g3(end);
h3 = h3(end);
i3 = i3(end);
j3 = j3(end);
k3 = k3(end);
l3 = l3(end);
m3 = m3(end);
n3 = n3(end);
o3 = o3(end);
p3 = p3(end);
q3 = q3(end);

%extract values for game with 2 groups
a4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_1.0.csv');
b4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_1.25.csv');
c4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_1.5.csv');
d4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_1.75.csv');
e4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_2.0.csv');
f4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_2.25.csv');
g4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_2.5.csv');
h4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_2.75.csv');
i4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_3.0.csv');
j4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_3.25.csv');
k4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_3.5.csv');
l4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_3.75.csv');
m4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_4.0.csv');
n4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_4.25.csv');
o4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_4.5.csv');
p4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_4.75.csv');
q4 = csvread('Group_Game_Nodes_128.0_Groups_2_b_5.0.csv');


%extract final values
a4 = a4(end);
b4 = b4(end);
c4 = c4(end);
d4 = d4(end);
e4 = e4(end);
f4 = f4(end);
g4 = g4(end);
h4 = h4(end);
i4 = i4(end);
j4 = j4(end);
k4 = k4(end);
l4 = l4(end);
m4 = m4(end);
n4 = n4(end);
o4 = o4(end);
p4 = p4(end);
q4 = q4(end);

usual_game = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];


cbr2 = [1,2, 3,5];
%17 steps from 1 to 5 at 0.25 increments
cbr = linspace(1,5,17);
%add final values to vector
pd_well_mixed_final_values = [a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q];
pd_groups_final_values_n2 = [a1,b1,c1,d1,e1,f1,g1,h1,i1,j1,k1,l1,m1...
    ,n1,o1,p1,q1];
pd_groups_final_values_n4 = [a2,b2,c2,d2,e2,f2,g2,h2,i2,j2,k2,l2,m2,...
    n2,o2,p2,q2];
pd_groups_final_values_n8 = [a3,b3,c3,d3,e3,f3,g3,h3,i3,j3,k3,l3,m3,...
    n3,o3,p3,q3];
pd_groups_final_values_2 = [a4,b4,c4,d4,e4,f4,g4,h4,i4,j4,k4,l4,m4,...
    n4,o4,p4,q4];
%plot vectors
plot(cbr, pd_well_mixed_final_values,'^-');
plot(cbr, pd_groups_final_values_n2,'o-');
plot(cbr, pd_groups_final_values_n4,'--x');
plot(cbr, pd_groups_final_values_n8, '--s');
plot(cbr, pd_groups_final_values_2, '--h');
plot(cbr, usual_game,'-d');
%set axis
axis([1, 5, 0 , 0.6]);
%set label
xlabel('b (defection potential)');
ylabel('fraction of cooperation');

legend('Model D' , 'Model E: N/2', 'Model E: N/4',...
    'Model E: N/8','Model E: 2', 'Model A');

title('b-variance in a Well Mixed Population Size: 128');