#include <stdio.h>

int arr_sum(int *arr, int n)
{
	int sum, i;
	sum = 0;

	for (i = 0; i < n; i++) {
		sum += arr[i];
	}
	return sum;
}

int main(void)
{
	int arr1[] = { 10, 20, 30, 40, 50 };
	printf("%d\n", arr_sum(arr1, 5));
	return 0;
}
