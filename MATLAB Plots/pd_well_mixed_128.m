clear
close all
clc
hold on

title('N = 128, b = 2.0');

a = csvread('Groups_PD_1_Nodes_p_Gr.csv');
b = csvread('Groups_PD_2_Nodes_p_Gr.csv');
c = csvread('Groups_PD_4_Nodes_p_Gr.csv');
d = csvread('Groups_PD_8_Nodes_p_Gr.csv');
e = csvread('Groups_PD_16_Nodes_p_Gr.csv');
f = csvread('Groups_PD_32_Nodes_p_Gr.csv');
g = csvread('Groups_PD_64_Nodes_p_Gr.csv');
h = csvread('SingleStrategyWellMixed128.csv');

plot(a,'-.k');
plot(b,'--gr');
plot(c,'--r');
plot(d,'--');
plot(e,'--');
plot(f,'--');
plot(g,'--b');
plot(h,'-m');

legend('N Strategies', 'N/2 strategies', 'N/4 Strategies', ...
    'N/8 Strategies', 'N/16 Strategies', 'N/32 Strategies',...
    '2 Strategies', 'Single Strategy');

xlabel('Time');
ylabel('Fraction of cooperation');

%add labels and stuff