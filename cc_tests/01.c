int main() {

	int x = 1;

	if(x == 1) {
		x = 1;
		again:
		x = 32;
	} else if(x == 2) {
		x = 2;
	} else if(x == 3) {
		x = 3;
	} else {

	}
	goto again;
	return x;
}

