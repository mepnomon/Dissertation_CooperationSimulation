clear
close all
clc
hold on


m = csvread('1DLowCognition_RingData.csv');
n = csvread('ring_structured_cooperation.csv');
o = csvread('simultaneous_cooptotal.csv');
%m = csvread('File1.csv');
%n = csvread('File2.csv');
%o = csvread('File3.csv');
plot(m,'-b');
plot(n,'-g');
plot(o,'-c');
%n = m;
%n(:) = 200 - m(:);
%plot(n,'.r');
%n = csvread('mixed_Data.csv');
%plot(n,'r.');
%title('Comparison of Simultaneous Strategy Interactions, Population: 100');
title('Comparison of single and two strategies, R@1.9, C@1');
legend('Low cognition', 'Single strategy', 'Two Strategies');
xlabel('Interactions');
ylabel('% of cooperators');
axis([0, 9999, 0 , 1]);