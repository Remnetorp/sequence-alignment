# sequence-alignment
-----------------------------------------------------------------------------------------------------------------------------
This is a program to find the most optimal alignment of two strings. The two strings can be viewed as words or just sequences.
Furthermore the program decides most optimal alignment depending on a matrix with the cost of aligning each pair of letters, 
i.e. the gain of aligning ”A” with ”B” and so on. Given these strings the algorithm finds an optimal alignment, such that the
total gain of aligning the strings in maximized. When aligning the two strings it is allowed to insert certain ”*”-characters
in one or both of the strings at as many positions in the string as required. Each such insertion can be done to a cost of −4.
No letters in either of the strings can be changed, moved or removed – the only allowed modification is to insert ”*”.

------------------------------------------------------------------------------------------------------------------------------
THe required syntax of the input for the program is that the first line contains a number of space-separated characters, 
c1, ..., ck – the characters that will be used in the strings. Then follows k lines with k space-separated integers where the
jth integer on the ith row is the cost of aligning ci and cj. Then follows one line with an integer Q(1 ≤ Q ≤ 10), the number
of queries that you will solve. Then follows Q lines, each describing one query. Each of these lines contain two space-separated
strings,the strings that should be aligned with maximal gain. It is guaranteed that each string in all queries has size at most 3500.
