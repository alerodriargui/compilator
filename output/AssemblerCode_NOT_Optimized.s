.global main
.extern printf, scanf
.data
T23: .asciz "FACTORIAL "
T24: .asciz "! :"
T27: .asciz "RECURSIVE ADDING "
T26: .asciz "\n"
T28: .asciz " :"
T30: .asciz "\n"
T12: .asciz "\n"
T11: .asciz "Invalid number: "
format_int: .asciz "%d"
true_label : .asciz "true"
false_label : .asciz "false"
.text
# PROC_factorial:skip
PROC_factorial:

# pmb PROC_factorial
push %rbp        # Guardem el registre que utilitzarem com a apuntador de la pila.
mov %rsp, %rbp
sub $34, %rsp

# T0 = 0
movl $0, %edi
movl %edi, -4(%rbp)

# result_0_2 = T0
movl -4(%rbp), %edi
movl %edi, -8(%rbp)

# T1 = 0
movl $0, %edi
movl %edi, -12(%rbp)

# T2= num_0_1 EQ T1
movl -12(%rbp), %edi
movl 16(%rbp), %esi
xor %rax, %rax # clean return value register
call CMP_EQ_NUM
movw %ax,-14(%rbp) # get return value

# if T2=true goto LABEL_0
cmpw $1,-14(%rbp)
je LABEL_0

# go_to LABEL_1
jmp LABEL_1

# LABEL_0:skip
LABEL_0:

# T3 = 1
movl $1, %edi
movl %edi, -18(%rbp)

# result_0_2 = T3
movl -18(%rbp), %edi
movl %edi, -8(%rbp)

# go_to LABEL_2
jmp LABEL_2

# LABEL_1:skip
LABEL_1:

# T4 = 1
movl $1, %edi
movl %edi, -22(%rbp)

# T5 = num_0_1 sub T4
movl 16(%rbp), %edi
movl -22(%rbp), %eax
subl %eax, %edi
movl %edi, -26(%rbp)

# param PROC_factorial(T5)
movslq -26(%rbp), %rdx
push %rdx

# call PROC_factorial
xor %rax, %rax   # clean return register
call PROC_factorial
# pop all params
pop %rdx

# T6 = return PROC_factorial
movl %eax, -30(%rbp)

# T7 = num_0_1 prod T6
movl 16(%rbp), %edi
movl -30(%rbp), %eax
imull %eax, %edi
movl %edi, -34(%rbp)

# result_0_2 = T7
movl -34(%rbp), %edi
movl %edi, -8(%rbp)

# LABEL_2:skip
LABEL_2:

# rtn result_0_2
# Moving function result into %eax or %ax
movl -8(%rbp), %eax
# Delete all reserved space
addq $34, %rsp
leave
ret

# PROC_recursiveAdding:skip
PROC_recursiveAdding:

# pmb PROC_recursiveAdding
push %rbp        # Guardem el registre que utilitzarem com a apuntador de la pila.
mov %rsp, %rbp
sub $44, %rsp

# T8 = 0
movl $0, %edi
movl %edi, -4(%rbp)

# result_1_2 = T8
movl -4(%rbp), %edi
movl %edi, -8(%rbp)

# T9 = 1
movl $1, %edi
movl %edi, -12(%rbp)

# T10= num_0_1 LT T9
movl -12(%rbp), %edi
movl 16(%rbp), %esi
xor %rax, %rax # clean return value register
call CMP_LT
movw %ax,-14(%rbp) # get return value

# if T10=true goto LABEL_3
cmpw $1,-14(%rbp)
je LABEL_3

# go_to LABEL_4
jmp LABEL_4

# LABEL_3:skip
LABEL_3:

# T11 = "Invalid number: "
# output T11
mov $T11, %rdi
xor %rax, %rax
call printf

# output num_0_1
mov $format_int, %rdi
xor %rsi, %rsi
movl 16(%rbp), %esi
xor %rax, %rax
call printf

# T12 = "\n"
# output T12
mov $T12, %rdi
xor %rax, %rax
call printf

# T13 = 0
movl $0, %edi
movl %edi, -18(%rbp)

# result_1_2 = T13
movl -18(%rbp), %edi
movl %edi, -8(%rbp)

# go_to LABEL_5
jmp LABEL_5

# LABEL_4:skip
LABEL_4:

# T14 = 1
movl $1, %edi
movl %edi, -22(%rbp)

# T15= num_0_1 EQ T14
movl -22(%rbp), %edi
movl 16(%rbp), %esi
xor %rax, %rax # clean return value register
call CMP_EQ_NUM
movw %ax,-24(%rbp) # get return value

# if T15=true goto LABEL_6
cmpw $1,-24(%rbp)
je LABEL_6

# go_to LABEL_7
jmp LABEL_7

# LABEL_6:skip
LABEL_6:

# T16 = 1
movl $1, %edi
movl %edi, -28(%rbp)

# result_1_2 = T16
movl -28(%rbp), %edi
movl %edi, -8(%rbp)

# LABEL_5:skip
LABEL_5:

# go_to LABEL_8
jmp LABEL_8

# LABEL_7:skip
LABEL_7:

# T17 = 1
movl $1, %edi
movl %edi, -32(%rbp)

# T18 = num_0_1 sub T17
movl 16(%rbp), %edi
movl -32(%rbp), %eax
subl %eax, %edi
movl %edi, -36(%rbp)

# param PROC_recursiveAdding(T18)
movslq -36(%rbp), %rdx
push %rdx

# call PROC_recursiveAdding
xor %rax, %rax   # clean return register
call PROC_recursiveAdding
# pop all params
pop %rdx

# T19 = return PROC_recursiveAdding
movl %eax, -40(%rbp)

# T20 = num_0_1 add T19
movl 16(%rbp), %edi
movl -40(%rbp), %eax
addl %eax, %edi
movl %edi, -44(%rbp)

# result_1_2 = T20
movl -44(%rbp), %edi
movl %edi, -8(%rbp)

# LABEL_8:skip
LABEL_8:

# rtn result_1_2
# Moving function result into %eax or %ax
movl -8(%rbp), %eax
# Delete all reserved space
addq $44, %rsp
leave
ret

# main:skip
main:

# pmb PROC_main
push %rbp        # Guardem el registre que utilitzarem com a apuntador de la pila.
mov %rsp, %rbp
sub $24, %rsp

# T21 = 5
movl $5, %edi
movl %edi, -4(%rbp)

# numA_2_2 = T21
movl -4(%rbp), %edi
movl %edi, -8(%rbp)

# T22 = 10
movl $10, %edi
movl %edi, -12(%rbp)

# numB_2_2 = T22
movl -12(%rbp), %edi
movl %edi, -16(%rbp)

# T23 = "FACTORIAL "
# output T23
mov $T23, %rdi
xor %rax, %rax
call printf

# output numA_2_2
mov $format_int, %rdi
xor %rsi, %rsi
movl -8(%rbp), %esi
xor %rax, %rax
call printf

# T24 = "! :"
# output T24
mov $T24, %rdi
xor %rax, %rax
call printf

# param PROC_factorial(numA_2_2)
movslq -8(%rbp), %rdx
push %rdx

# call PROC_factorial
xor %rax, %rax   # clean return register
call PROC_factorial
# pop all params
pop %rdx

# T25 = return PROC_factorial
movl %eax, -20(%rbp)

# output T25
mov $format_int, %rdi
xor %rsi, %rsi
movl -20(%rbp), %esi
xor %rax, %rax
call printf

# T26 = "\n"
# output T26
mov $T26, %rdi
xor %rax, %rax
call printf

# T27 = "RECURSIVE ADDING "
# output T27
mov $T27, %rdi
xor %rax, %rax
call printf

# output numB_2_2
mov $format_int, %rdi
xor %rsi, %rsi
movl -16(%rbp), %esi
xor %rax, %rax
call printf

# T28 = " :"
# output T28
mov $T28, %rdi
xor %rax, %rax
call printf

# param PROC_recursiveAdding(numB_2_2)
movslq -16(%rbp), %rdx
push %rdx

# call PROC_recursiveAdding
xor %rax, %rax   # clean return register
call PROC_recursiveAdding
# pop all params
pop %rdx

# T29 = return PROC_recursiveAdding
movl %eax, -24(%rbp)

# output T29
mov $format_int, %rdi
xor %rsi, %rsi
movl -24(%rbp), %esi
xor %rax, %rax
call printf

# T30 = "\n"
# output T30
mov $T30, %rdi
xor %rax, %rax
call printf

# rtn
# Delete all reserved space
addq $24, %rsp
leave
ret

# exit

# auxiliar functions
# boolean value assignation EQ
CMP_EQ :
	cmp %di, %si
	jne CMP_EQ_NE
	mov $1, %rax
	ret
CMP_EQ_NE :
	mov $0, %rax
	ret

# boolean value assignation NE
CMP_NE :
	cmp %di, %si
	je CMP_NE_E
	mov $1, %rax
	ret
CMP_NE_E :
	mov $0, %rax
	ret

# boolean value assignation GT
CMP_GT :
	cmp %edi, %esi
	jle CMP_GT_LE
	mov $1, %ax
	ret
CMP_GT_LE :
	mov $0, %ax
	ret

# boolean value assignation GE
CMP_GE :
	cmp %edi, %esi
	jl CMP_GE_L
	mov $1, %ax
	ret
CMP_GE_L :
	mov $0, %ax
	ret

# boolean value assignation LE
CMP_LE :
	cmp %edi, %esi
	jg CMP_LE_G
	mov $1, %ax
	ret
CMP_LE_G :
	mov $0, %ax
	ret

# boolean value assignation LT
CMP_LT :
	cmp %edi, %esi
	jge CMP_LT_GE
	mov $1, %ax
	ret
CMP_LT_GE :
	mov $0, %ax
	ret

# boolean value assignation NE num
CMP_NE_NUM :
	cmp %edi, %esi
	je CMP_NE_E_NUM
	mov $1, %ax
	ret
CMP_NE_E_NUM :
	mov $0, %ax
	ret

# boolean value assignation EQ num
CMP_EQ_NUM :
	cmp %edi, %esi
	jne CMP_EQ_NE_NUM
	mov $1, %ax
	ret
CMP_EQ_NE_NUM :
	mov $0, %ax
	ret

print_bool :
cmpw $0,%di
je print_false
mov $true_label, %rdi
jmp print_bool_val
print_false : mov $false_label, %rdi
print_bool_val : xor %rax, %rax
call printf
ret
