clear
close all
clc
hold on
%%this function is used to compare all three cost/benefit ratios
axis([0, 1, 0 , 1]);
cbr = csvread('CBRValues.csv');

a = [.945,.99,.45,.33;cbr]; %two strategies
b = [1,1,0.93,0;cbr];         %single strategy
c = [1, 1, 1, 0.05;cbr];             %low cognitive
plot(a(1,:),a(2,:),'^r');
plot(b(1,:),b(2,:),'.k','MarkerSize', 18);
plot(c(1,:),b(2,:),'dg');

title('Structured Pop: 100, Interactions: 10,000, 1D Lattice');
legend('Two Strategies', 'Single Strategy', 'Low Cognition');
ylabel('CBR');
xlabel('% cooperators');