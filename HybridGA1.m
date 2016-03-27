% To run this file, just type the file name 'HybridGA1' at the command
% prompt This is the Hybrid Genetic Algorithm-1 The 10% of the initial
% population is filled with the top routes from the Nearest Neighbor
% Algorithm solutions

function varargout = tsp(varargin)
%To Define the Functions at the prompt or in scripts
rand('state', 0);
% The parameters for the GA-TSP are defined here
%pop_size is the Population Size
%num_iter is the Number of Generations
%mutate_rate is the Mutation Rate
%n_cities is the Number of Cities
%cities is the 2 dimensional matrix holding (x,y) coordinates of n_cities
pop_size = 100; num_iter = 500; mutate_rate = 0.85;
n_cities = 90; cities = 10*rand(n_cities, 2);


% Construct the Distance Matrix
%This is a 2 Dimensional Matrix giving diatance between any two cities
dist_matx = zeros(n_cities);
for ii = 2:n_cities
    for jj = 1:ii-1
        dist_matx(ii, jj) = sqrt(sum((cities(ii, :)-cities(jj, :)).^2));
        dist_matx(jj, ii) = dist_matx(ii, jj);
    end
end

% Plot Cities in a Figure
    figure(1)
    plot(cities(:,1), cities(:,2), 'b.')
        for c = 1:n_cities
            text(cities(c, 1), cities(c, 2), [' ' num2str(c)])
        end
    title([num2str(n_cities) ' Cities TSP'])

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Nearest Neighbour Algorithm
%

nn_route = zeros(n_cities, n_cities);
nn_distance= zeros(1,n_cities);

for nn_count1=1:n_cities
    nn_cities = cities;
    min_dist=1000;
    temp_min=0;
    min_ind=nn_count1;
    nn_matrix=dist_matx;
    nn_count=0;
    nn_times=0;
    nn_index=1;
    nn_route(nn_count1,1)=nn_count1;
    
    for nn_loop4=1:n_cities
        nn_matrix(nn_loop4,nn_count1)=0;
    end

    while (nn_count ~=nn_count1)
    nn_count=min_ind;
    for nn_loop2=1:n_cities % For getting the nearest Neighbour
            dis1=nn_matrix(nn_count,nn_loop2);
            if(dis1 ~= 0)
                temp_min=dis1;
                if(temp_min < min_dist)
                    min_dist=temp_min;
                    min_ind=nn_loop2;
                end                
            end
            if(nn_loop2 == n_cities)
                nn_index = nn_index+1;
                nn_route(nn_count1,nn_index) = min_ind;
%                 min_dist
                nn_distance(nn_count1) = nn_distance(nn_count1) + min_dist;
                nn_times = nn_times+1;
                Nextcity = min_ind;
                min_dist=1000;
                for nn_loop3=1:n_cities
                    nn_matrix(nn_loop3,min_ind)=0;
                end
%                 nn_count1=min_ind;
                nn_count=min_ind;
                if(nn_times == n_cities-1)
                    nn_count=nn_count1;
                end
            end            
    end
    end
    nn_distance(nn_count1) = nn_distance(nn_count1) + dist_matx(min_ind,nn_count1);
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% min_ind
% nn_count1
% dist_matx
%     
% nn_distance
% 
% nn_route        

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Find Best Route among the all Nearest Neighbour Routes
%
[nn_D,nn_I] = min(nn_distance);
% nn_D
% nn_I
% nn_route(nn_I,:)

nn_best_route = nn_route(nn_I,:);
nn_best_route;
%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%Sorting the Nearest Neighbour Routes
%
[nn_sorted_distance,nn_IX] = sort(nn_distance);
nn_sorted_distance
nn_IX
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%Plotting the Best Nearest Neighbour Route
%
    figure(2)
    plot_nn_route = cities([nn_best_route nn_best_route(1)], :);
    plot(plot_nn_route(:, 1), plot_nn_route(:, 2)', 'b-')
    for c = 1:n_cities
            text(cities(c, 1), cities(c, 2), [' ' num2str(c)])
    end
    title(['Best NN Route starting from City-' num2str(nn_I) ' (dist = ' num2str(nn_D) ')'])
    
    
%
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% Initialize Population with random generations
% The first chromosome is the series 1,2,3....N
% The rest are just the random permutations of N
pop = zeros(pop_size, n_cities);
pop(1, :) = (1:n_cities);

%Initializing 2-11 with top 10 best routes from NN
entry = 0;
for k = 2:pop_size
%     nn_IX(k-1)
%     nn_route(nn_IX(k-1),:)
    entry = entry +1;
    if(entry > 10)
        pop(k, :) = randperm(n_cities); 
    else
        pop(k, :) = nn_route(nn_IX(entry),:);
    end
end

% Intializing all of the initial population with the top 10 NN routes
% This section is for HybridGA2, Complete Filling
% entry = 0;
% for k = 2:pop_size
%     entry = entry +1;
%     if(entry > 10)
%         entry=1;
%     end
%     pop(k, :) = nn_route(nn_IX(entry),:);
% end


% This section is for PureGA
% Initializing the entire population with random chromosomes
% for k = 2:pop_size    
%     pop(k, :) = randperm(n_cities);    
% end

%Fitness for each chromosome is calculated just by calculating the TOUR
%distance which is just the summation of the distances between the adjacent
%cities
fitness = zeros(1, pop_size);
best_fitness = zeros(1, num_iter);
for iter = 1:num_iter
    for p = 1:pop_size
        d = dist_matx(pop(p, 1), pop(p, n_cities));
        for city = 2:n_cities
            d = d + dist_matx(pop(p, city-1), pop(p, city));
        end
        fitness(p) = d;
    end
    [best_fitness(iter) index] = min(fitness);
    best_route = pop(index, :);
% The best fitness is stored as the best_fitness for that generation
    
     
    % Genetic Algorithm Search for the best TSP Solution
    pop = iterative_algorithm(pop, fitness, mutate_rate);
end

    figure(3)
    route = cities([best_route best_route(1)], :);
    plot(route(:, 1), route(:, 2)', 'b-')
    for c = 1:n_cities
            text(cities(c, 1), cities(c, 2), [' ' num2str(c)])
    end
    title(['Best Hybrid GA-I Route (distance = ' num2str(best_fitness(iter)) ')'])
    figure(4)
    plot(best_fitness(1:iter), 'b', 'LineWidth', 2)
    title('Best Fitness - Hybrid GA-I')
    xlabel('Generation')
    ylabel('Distance')
    axis([1 max(2, iter) 0 max(best_fitness)*1.1])



[not_used indx] = min(best_route);
best_ga_route = [best_route(indx:n_cities) best_route(1:indx-1)];
if best_ga_route(2) > best_ga_route(n_cities)
    best_ga_route(2:n_cities) = fliplr(best_ga_route(2:n_cities));
end
varargout{1} = cities(best_ga_route, :);
varargout{2} = best_ga_route;
varargout{3} = best_fitness(iter);


% GENETIC ALGORITHM FUNCTION
% This function is repeatedly called till the termination condition is
% satisfied

function new_pop = iterative_algorithm(pop, fitness, mutate_rate)

[p, n] = size(pop);

% Tournament Selection - Round One
% Here we generate the winners for every two genomes selected randomly
% and they are stored in a temporary pool of population and fitness
% of them are stored in a temporary array
new_pop = zeros(p, n);
ts_r1 = randperm(p);
winners_r1 = zeros(p/2, n);
tmp_fitness = zeros(1, p/2);
for i = 2:2:p
    if fitness(ts_r1(i-1)) > fitness(ts_r1(i))
        winners_r1(i/2, :) = pop(ts_r1(i), :);
        tmp_fitness(i/2) = fitness(ts_r1(i));
    else
        winners_r1(i/2, :) = pop(ts_r1(i-1), :);
        tmp_fitness(i/2) = fitness(ts_r1(i-1));
    end
end

% Tournament Selection - Round Two
% Here too we generate the winners for every two genomes selected randomly
% from the temporary pool of population and they are stored in another
% temporary pool of popultaion

ts_r2 = randperm(p/2);
winners = zeros(p/4, n);
for i = 2:2:p/2
    if tmp_fitness(ts_r2(i-1)) > tmp_fitness(ts_r2(i))
        winners(i/2, :) = winners_r1(ts_r2(i), :);
    else
        winners(i/2, :) = winners_r1(ts_r2(i-1), :);
    end
end
%Elites are Passed Over to new generation
new_pop(1:p/4, :) = winners;
new_pop(p/2+1:3*p/4, :) = winners;

% Crossover
% The parents for crossover are taken from the first temporary pool
% of popultaion
% Every pair of parents are chosen randomly from winners_r1 array
% just by using randperm function.
crossover = randperm(p/2);
children = zeros(p/4, n);
for i = 2:2:p/2
    parent1 = winners_r1(crossover(i-1), :);
    parent2 = winners_r1(crossover(i), :);
    child = parent2;
    % This rand generates 2 decimal values and they are sorted and
    % multiplied by n and rounded off to bigger number to use them as index
    % for the array
    ndx = ceil(n*sort(rand(1, 2)));
    while ndx(1) == ndx(2)
        ndx = ceil(n*sort(rand(1, 2)));
    end
    %The index selcetion is repeated till the 2 indices are different
    tmp = parent1(ndx(1):ndx(2));
    %Choose the part of parent1 between index 1 and 2 as temp
    for j = 1:length(tmp)
        child(find(child == tmp(j))) = 0;
    end
    %Zeros the cities in child which is already got from parent 2 
    % and the tmp is sandwitched inside the child
    child = [child(1:ndx(1)) tmp child(ndx(1)+1:n)];
    child = nonzeros(child)';
    %The nonnzero entries are filtered and used as children
    children(i/2, :) = child;
end
new_pop(p/4+1:p/2, :) = children;
new_pop(3*p/4+1:p, :) = children;   

% Mutate
%Mutate the pop(num) chromosome The procedure is to take some random places
%in the chromosome and interchange or reverse(flip) them in order to mutate
%at the same time to preserve the validity
mutate = randperm(p/2);
num_mutate = round(mutate_rate*p/2);
% This rnad generates 2 decimal values and they are sorted and
% multiplied by n and rounded off to bigger number to use them as index
% for the array
for i = 1:num_mutate
    ndx = ceil(n*sort(rand(1, 2)));
    while ndx(1) == ndx(2)
        ndx = ceil(n*sort(rand(1, 2)));
    end
    %The index selcetion is repeated till the 2 indices are different
    new_pop(p/2+mutate(i), ndx(1):ndx(2)) = ...
        fliplr(new_pop(p/2+mutate(i), ndx(1):ndx(2)));
    %The region between the indices are flipped left to right
end

