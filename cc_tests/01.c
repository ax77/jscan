int main() {
	int x = 1;
	if(x) {
		goto out;
	}
	x = 0;
	out:
	return x;
}

