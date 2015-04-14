function convertARFF(dataset, trorte, trorte2)
filename = strcat('C:\Users\User\git\Masterproef\Masterproef\', ...
    'datasets\',dataset,'\',trorte,'\',trorte2,'.arff');

wekaOBJ = loadARFF(filename);

n = 0;
for i = 0:wekaOBJ.numInstances-1
    n = n + wekaOBJ.instance(i).numValues;
end
ii = zeros(n,1);
jj = zeros(n,1);
vv = zeros(n,1);

k = 1;
for i=0:wekaOBJ.numInstances-1
    instance = wekaOBJ.instance(i);
    for j = 0:instance.numValues-1
        ii(k) = i+1;
        jj(k) = instance.index(j)+1;
        vv(k) = instance.valueSparse(j);
        k=k+1;
    end
end
ijvmatrix = [ii, jj, vv];
filename2 = strcat('data/',char(dataset),'-',char(trorte),'-',char(trorte2),'.mat');
save(filename2,'ijvmatrix');

end

