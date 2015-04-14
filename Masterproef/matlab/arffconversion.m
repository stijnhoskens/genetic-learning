
datasets = {'20ng';'classic';'cora';'dmoz';'movies'; ...
    'r52';'rcv1';'webkb';'wipo'};
evo = {'evoTrain';'evoTest'};
trorte = {'train';'test'};
for i = 1:size(datasets)
    for j = 1:size(evo)
        for k = 1:size(trorte)
            convertARFF(datasets(i),evo(j),trorte(k));
            disp(strcat(datasets(i),'-',evo(j),'-',trorte(k),'.mat saved.'));
        end
    end
end
