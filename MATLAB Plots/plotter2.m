clear
close all
clc
hold on
axis([0, 2, 0 , 1]);
m = csvread('StructuredCB_tweaked.csv');
n = csvread('StructuredCFraction_tweaked.csv');
x = csvread('SimultaneousCB_tweaked.csv');
y = csvread('simultaneousfraction_tweaked.csv');
plot(m,n, '^-r');
plot(x, y, 'o-b');
title('CBR Comparison - Ring (r@1.9, c@0.19)');
legend('Structured Ring', 'Single Strategy');
xlabel('Fraction of cooperation');
ylabel('Pc');