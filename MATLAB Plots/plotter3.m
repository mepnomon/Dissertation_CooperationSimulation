clear
close all
clc
hold on

axis([0, 10, 0 , 1]);
a = [1.9,3.8,7.6,10;0.33,0.45,0.99,0.945]; %two strategies
b = [1.9,3.8,7.6,10;0,0.93,1,1];
plot(a(1,:),a(2,:),'^r');
plot(b(1,:),b(2,:),'.k','MarkerSize', 18);

title('Population Size: 100, Interactions: 10,000');
legend('Two Strategies', 'Single Strategy');
ylabel('% cooperators');
xlabel('Benefit/Cost Ratio');