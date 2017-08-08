clear
close all
clc
hold on
cbr = linspace(0, 2, 21);
axis([0, 1.9, 0 , 1]);

a = csvread('varying_b_Groups_1_.csv');
b = csvread('varying_b_Groups_2_.csv');
c = csvread('varying_b_Groups_4_.csv');
d = csvread('varying_b_Groups_8_.csv');
e = csvread('varying_b_Groups_16_.csv');
f = csvread('varying_b_Groups_32_.csv');
g = csvread('varying_b_Groups_64_.csv');
h = csvread('varying_b_Groups_128_.csv');

plot(cbr, a,'-*r');
plot(cbr, b,'-*b');
plot(cbr, c,'-*b');
plot(cbr, d,'-*b');
plot(cbr, e,'-*b');
plot(cbr, f,'-*b');
plot(cbr, g,'-*b');
plot(cbr, h,'-*b');

