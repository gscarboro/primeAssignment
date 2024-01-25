# primeAssignment
Prime finder homework assignment for COP4520, Spring 2024

Project Description: 
Your non-technical manager assigns you the task to find all primes between 1 and
10^8 (100,000,000). The assumption is that your company is going to use a parallel machine that
supports eight concurrent threads. Thus, in your design you should plan to spawn 8
threads that will perform the necessary computation. Your boss does not have a strong
technical background but she is a reasonable person. Therefore, she expects to see that
the work is distributed such that the computational execution time is approximately
equivalent among the threads. Finally, you need to provide a brief summary of your
approach and an informal statement reasoning about the correctness and efficiency of
your design. Provide a summary of the experimental evaluation of your approach.
Remember, that your company cannot afford a supercomputer and rents a machine by
the minute, so the longer your program takes, the more it costs. Feel free to use any
programming language of your choice that supports multi-threading as long as you
provide a ReadMe file with instructions for your manager explaining how to compile and
run your program from the command prompt.

My Approach:
For multithreading, I used a counter class in order to increment which number each thread was
checking next. Atomic numbers were used in order to ensure the stability and integrity of the
variables that would be accessed by all threads. This ensured that multiple threads weren't reading
and/or writing to them concurrently. As far as the calculation goes, I knew my method of calculating
primality would need to be somewhat efficient, as Java can be slower than C++ or Rust for things
like this. I settled on the trial division method, in which we look at only numbers up to the 
square root of the number we are checking for its divisors. I didn't fully believe that this
would work until I read up on it some more (and triple checked my output) but it seems to work
well for this application. There are other (at least 1), more efficient algorithms like Sieve of
Eratosthenes, but I settled on trial division as I found it to be adequate for this project and 
simpler to implement (way simpler!). During testing, I also eventually realized that incrementing
by 2 (in order to skip even numbers) could drastically reduce the runtime.

Correctness and Efficiency:
I double checked that the output was the same after running multiple times, and that the execution
time stayed somewhat constant (~19.5s for me on my laptop). I also checked on 'prime checker'
websites in order to check that the final count and top ten primes were accurate. 

Running the Code:
In a terminal, 
Input 'javac primeAssignment.java' to compile the code.
Input 'java primeAssignment' to run the program.

Then, after about 20 seconds you should see that the 'primes.txt' file has appeared and it will
have the desired data within.
