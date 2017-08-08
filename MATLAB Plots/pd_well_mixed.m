clear
close all
clc
hold on
axis([0, 100000, 0 , .6]);
a = csvread('Groups_PD_1_Nodes_p_Gr.csv'); %% # of groups
b = csvread('Groups_PD_2_Nodes_p_Gr.csv'); %N/2 strategies
c = csvread('Groups_PD_4_Nodes_p_Gr.csv');
d = csvread('Groups_PD_8_Nodes_p_Gr.csv'); %8 strategies are 128 groups = N/4
e = csvread('Groups_PD_16_Nodes_p_Gr.csv');
f = csvread('Groups_PD_32_Nodes_p_Gr.csv');
g = csvread('Groups_PD_64_Nodes_p_Gr.csv');
h = csvread('Groups_PD_128_Nodes_p_Gr.csv');%N/8
i = csvread('Groups_PD_256_Nodes_p_Gr.csv'); %N/4
j = csvread('Groups_PD_512_Nodes_p_Gr.csv'); %2 strategies
k = csvread('SingleStrategyWellMixed1028.csv'); %single strategy

%a(:,end) = a(:,end) * 100;
%rotate3d
plot(a,'-.k');
plot(b,'--gr'); %N/2 strategies

%plot(c,'--r');
%plot(d,'--b'); %N/8
%plot(e,'--');
%plot(f,'--');
%plot(g,'--');%N/20
plot(h,'--'); %N/8
plot(i,'--'); %N/4
plot(j,'--'); %2 strategies
plot(k,'-m');

legend('N Strategies', 'N/2 Strategies', 'N/8 Strategies',...
    'N/4 Strategies', '2 strategies', 'Single Strategy');
title('N=1028 b = 2.0');
% # of nodes in a group
%legend('N Strategies f. pop.','N/2 Groups','4 Nodes p. Group' ,...
%'8 Nodes p. Group','16 Nodes p. Group','32 Nodes p. Group',...
%'64 Nodes p. Group','128 Nodes p. Group', '256 Nodes p. Group',...
%'512 Nodes p. Group', 'Single Strategy');

%legend('N Strategies','N/2 Strategies ','4 Nodes p. Group' ,...
%'8 Nodes p. Group','16 Nodes p. Group','N/4 Strategies (32 Nodes p. Group)',...
%'64 Nodes p. Group','128 Nodes p. Group', '256 Nodes p. Group',...
%'512 Nodes p. Group', 'Single Strategy');
%htitle = get(h,'Nodes in group');
%set(htitle,'String','Nodes in group')
%legend([a,b],aLabel);
xlabel('Time');
ylabel('Fraction of cooperation');
ax.YMinorGrid = 'on';
