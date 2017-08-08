clear
close all
clc
hold on

aLabel = '2 Nodes';

a = csvread('Groups_PD_Mean_64.csv'); %% # of groups
b =  csvread('Groups_PD_Mean_32.csv');
c = csvread('Groups_PD_Mean_16.csv');
d = csvread('Groups_PD_Mean_8.csv');
e = csvread('Groups_PD_Mean_4.csv');
f =  csvread('Groups_PD_Mean_2.csv');
%rotate3d
plot(a,'--');
plot(b,'b');
plot(c,'--r'); %dashed
plot(d,':','MarkerSize',32);
plot(e);
plot(f,'--k');

title('Level of cooperation in population at b = 2.0');
% # of nodes in a group
legend('64 Groups x 2 Nodes per group', '32G x 4N', '16G x 8N', '8G x 16N','4G x 32N', '2G x 64N');
%htitle = get(h,'Nodes in group');
%set(htitle,'String','Nodes in group')
%legend([a,b],aLabel);
xlabel('Game Time Steps');
ylabel('Fraction of cooperation');
axis([0, 100000, 0 , .6]);

