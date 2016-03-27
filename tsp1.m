function [distance] = tsp1(x)



h=[0, 1600, 1900, 1700, 2300, 2100, 2500, 2400, 2700, 2600, 2900, 1267, 1260, 1570,1400, 4345;
   1600, 0, 2000, 2500, 1900, 2900, 2000, 2500, 2700, 3000, 2800, 1460, 1254, 3434, 2354, 3433,;
   2100, 1900, 2500, 1900, 1700, 2400, 2700, 3000, 2300, 2500, 2900, 1244, 1343, 1345, 1289, 1234;
   1700, 2000, 2400, 2000, 1800, 2300, 2500, 2000, 2500, 2000, 2800, 1435, 1234, 2345, 1342, 2312;
   2200, 1800, 3500, 3100, 2300, 2400, 1800, 3100, 3200, 2300, 2000, 1243, 1342, 2343, 2121,2321;
   1900, 2100, 2600, 2600, 2300, 3000, 3500, 3100, 2300, 2600, 2500, 2312, 1233, 1233, 3234, 2312;
   1700, 1400, 2300, 2900, 2400, 2800, 1800, 3500, 2600, 2000, 3200, 3443, 2323, 2343, 2323,1232;
   2300, 2500, 2400, 3100, 3000, 2600, 3000, 2300, 3000, 2500, 2700, 2312, 1233, 1233, 3234,1323;
   2000, 1800, 2600, 2000, 2200, 3000, 2300, 2500, 2400, 2000, 2300, 2400, 2700, 3000, 2300,1111;
   2300, 1500, 2000, 2700, 2800, 2700, 3000, 2500, 2000, 2800, 2700, 1244, 1343, 1345, 1289, 1231;
   2000, 2300, 2500, 1500, 2500, 2000, 2300, 2600, 2000, 2500, 2000, 2400, 2700, 3000, 2300, 1333,
   1900, 2100, 2600, 2600, 2300, 3000, 3500, 3100, 2300, 2600, 2500, 2312, 1233, 1233, 3234, 1323;
   2100, 1900, 2500, 1900, 1700, 2400, 2700, 3000, 2300, 2500, 2900, 1244, 1343, 1345, 1289, 1444;
   1700, 1400, 2300, 2900, 2400, 2800, 1800, 3500, 2600, 2000, 3200, 3443, 2323, 2343, 2323, 1333;
   1700, 2000, 2400, 2000, 1800, 2300, 2500, 2000, 2500, 2000, 2800, 1435, 1234, 2345, 1342, 1222;
   1900, 2100, 2600, 2600, 2300, 3000, 3500, 3100, 2300, 2600, 2500, 2312, 1233, 1233, 3234, 1555
   ];

for i=1:16
    for j=1:16
        h1(i,j)=h(i,j);
    end
end

for i=1:16
    for j=1:16
        h1(i, i) = 0;
    end
end

for i=1:16
    for j=1:16
        h1(i, j) = h1(j, i);
    end
end

%unit = [];
%for i=1:16
%    unit = [unit ( (8*x(((i-1)*4 + 4))) + 4*x(((i-1)*4 + 3)) + 2*x(((i-1)*4 + 2)) + x(((i-1)*4 + 1)) )  ];
%end

%disp(unit);

disp(round(x));

distance = 0;
test = 0;
for i=1:16
    unit1 = round(x(i));
    if ((unit1 < 0) || (unit1 > 14))
        test = 1;
    end
end

disp(test);
if ((test ~= 1))
    for i=1:15
        distance = distance + h1((round(x(i)) + 1), ((round(x(i+1))+1))  ) ;
    end
else
    distance = 9e+100;
end

if ((test ~= 1))
    for i=1:16
        for j=(i+1):16
            if( (round(x(i))) == round(x(i+1)) )
                distance = 9e+100;
                %fprintf(1,' Two distances are equal\n');
                %disp(distance);
            end
        end
    end
end

%disp(h);
%disp(h1);




% Fitness function and numver of variables
%fitnessFcn = @(x) norm(x);
numberOfVariables = 16;

% If decision variables are bounded provide a bound e.g, LB and UB. 
LB = 0*ones(1,numberOfVariables);
UB = 16*ones(1,numberOfVariables); 
Bound = [LB;UB]; % If unbounded then Bound = []

% Create an options structure to be passed to GA
% Three options namely 'CreationFcn', 'MutationFcn', and
% 'PopInitRange' are required part of the problem.
options = gaoptimset('CreationFcn',@int_pop,'MutationFcn',@int_mutation, ...
    'PopInitRange',Bound,'Display','iter','StallGenL',40,'Generations',150, ...
    'PopulationSize',60,'PlotFcns',{@gaplotbestf,@gaplotbestindiv});

[x,fval] = ga(distance,numberOfVariables,options);
x
%---------------------------------------------------
% Mutation function to generate childrens satisfying the range and integer
% constraints on decision variables.
function mutationChildren = int_mutation(parents,options,GenomeLength, ...
    FitnessFcn,state,thisScore,thisPopulation)
shrink = .01; 
scale = 1;
scale = scale - shrink * scale * state.Generation/options.Generations;
range = options.PopInitRange;
lower = range(1,:);
upper = range(2,:);
scale = scale * (upper - lower);
mutationPop =  length(parents);
% The use of ROUND function will make sure that childrens are integers.
mutationChildren =  repmat(lower,mutationPop,1) +  ...
    round(repmat(scale,mutationPop,1) .* rand(mutationPop,GenomeLength));
% End of mutation function
%---------------------------------------------------
function Population = int_pop(GenomeLength,FitnessFcn,options)

totalpopulation = sum(options.PopulationSize);
range = options.PopInitRange;
lower= range(1,:);
span = range(2,:) - lower;
% The use of ROUND function will make sure that individuals are integers.
Population = repmat(lower,totalpopulation,1) +  ...
    round(repmat(span,totalpopulation,1) .* rand(totalpopulation,GenomeLength));
% End of creation function

