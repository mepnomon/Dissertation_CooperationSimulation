clear
close all
clc
hold on
%this function is used to compare all three cost/benefit ratios
axis([0, 1, 0, 1]);

cbr = linspace(0,1,191);
%cbr=fliplr(cbr);

csvread('CBRValues.csv'); %cost benefit ratios
%single = csvread('1DAvgSingleStrategy.csv'); 
%two = csvread('1DAvgTwoStrategy.csv');
%lowCognition = csvread('1DAvgLowCog.csv');
%replace = csvread('1DAvgReplaceModel.csv');

single = csvread('2DAvgSingleStrategy.csv'); 
two = csvread('2DAvgFourStrategies.csv');
lowCognition = csvread('2DAvgLowCognitionModel.csv');
replace = csvread('2DAvgReplaceModel.csv');

a = single;
b = two;
c = lowCognition;
d = replace;

plot(cbr,a(1,:),'.-');
plot(cbr,b(1,:),'.k-','MarkerSize', 18);
plot(cbr,c(1,:),'.g-');
plot(cbr,d(1,:),'.k-');

title('Method Comparison Averages on 2D Lattice');
legend('Single Strategy', 'Four Strategies', 'Low Cognition', 'Replace Model');
ylabel('pCooperators');
xlabel('Cost/Benefit');