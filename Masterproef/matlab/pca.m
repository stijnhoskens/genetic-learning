datasets = {'20ng';'classic';'cora';'dmoz';'movies'; ...
    'r52';'rcv1';'webkb';'wipo'};
trorte = {'evoTrain';'evoTest'};
trorte2 = 'train';
threshold = 0.8;
for i = 1:size(datasets)
    for j = 1:size(trorte)
        strcat(datasets(i), '-', trorte(j), '-', trorte2)
        A = getTDMatrix(datasets(i), trorte(j), trorte2);

        elements = nonzeros(A);
        sumofsquares = sqrt(sum(elements.^2));

        n = 10;
        s = svds(A,n);
        ratio = sqrt(sum(s.^2))/sumofsquares;
        ratio
    end
end