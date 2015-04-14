function TDM = getTDMatrix(dataset, trorte, trorte2)
filename = strcat('data/',char(dataset),'-', ...
    char(trorte),'-',char(trorte2),'.mat');
TDM = spconvert(importdata(filename));
end

