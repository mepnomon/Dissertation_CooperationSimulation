clear
close all
clc
hold on

axis([0, 100000, 0 , .6]);
a = csvread('Structured_2D_Multi_PopulationSize_ 100.0.csv');
b = csvread('SingleStrategyWellMixed.csv');
plot(a,'-.b');
plot(b,'-r');

legend('Model A', 'Usual game');
title('Performance Comparison ');
xlabel('Time');
ylabel('Fraction of cooperation');
ax.YMinorGrid = 'on';
