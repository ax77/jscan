int main() {
	int x = 70;
	int *z;
	z = &x;
	if(x == 1) {
		x = 1;
	} else if(x == 2) {
		x = 2;
	} else if(x == 3) {
		x = 3;
	} else {
		x = 1024;
	}
	return x + *z * 3;
}

