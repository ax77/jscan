// #include <stdio.h>
// #include "alist.h"
// #include "llist.h"


/* alist.h: a CPP-based template implementation of array lists (resizable array) */

#ifndef ALIST_H_INCLUDED
#define ALIST_H_INCLUDED

//#include <stdlib.h>

#define ALIST_PROTO(T, N) \
	typedef struct N N; \
	typedef int N##_iterator; \
	N *N##_new(void); \
	N *N##_new_cap(int size); \
	void N##_free(N *s); \
	int N##_size(const N *s); \
	int N##_insert(N *s, T item, int pos); \
	T N##_pop(N *s, int pos); \
	T N##_get(const N *s, int pos); \
	void N##_set(N *s, T item, int pos); \
	int N##_resize(N *s, int size); \
	N##_iterator N##_iterate(const N *s); \
	int N##_next(const N *s, N##_iterator *iter); \
	T N##_get_at(const N *s, N##_iterator iter); \
	void N##_set_at(N *s, T item, N##_iterator iter); \
	int N##_insert_at(N *s, T item, N##_iterator iter); \
	T N##_pop_at(N *s, N##_iterator iter)

/* defines functions for an arraylist with elements of type T named N */
#define ALIST(T, N) \
	struct N { int cap; int len; T *arr; }; \
	const int N##_sizeof_element = sizeof(T); \
	N *N##_new(void) \
	{ \
		return N##_new_cap(8); \
	} \
	N *N##_new_cap(int size) \
	{ \
		N *s; \
		s = malloc(sizeof(struct N)); \
		if (!s) return NULL; \
		s->cap = size; \
		s->len = 0; \
		s->arr = malloc(size * N##_sizeof_element); \
		if (!s->arr) { free(s); return NULL; } \
		return s; \
	} \
	void N##_free(N *s) \
	{ \
		free(s->arr); \
		free(s); \
	} \
	int N##_size(const N *s) \
	{ \
		return s->len; \
	} \
	int N##_insert(N *s, T item, int pos) \
	{ \
		T *temp; \
		int i; \
		if (s->len >= s->cap) { \
			temp = realloc(s->arr, s->cap*1.5*N##_sizeof_element); \
			if (!temp) return 0; \
			s->arr = temp; \
			s->cap *= 1.5; \
		} \
		if (pos >= 0 && pos != s->len) { \
			for (i=s->len; i>pos; --i) { \
				s->arr[i] = s->arr[i-1]; \
			} \
		} \
		s->arr[pos<0?s->len:pos] = item; \
		s->len++; \
		return 1; \
	} \
	T N##_pop(N *s, int pos) \
	{ \
		T temp; \
		int i; \
		if (pos < 0) return s->arr[s->len-1]; \
		temp = s->arr[pos]; \
		for (i=pos; i<s->len-1; ++i) { \
			s->arr[i] = s->arr[i+1]; \
		} \
		--s->len; \
		return temp; \
	} \
	T N##_get(const N *s, int pos) \
	{ \
		return s->arr[pos<0||pos>=s->len?s->len-1:pos]; \
	} \
	void N##_set(N *s, T item, int pos) \
	{ \
		s->arr[pos<0||pos>=s->len?s->len-1:pos] = item; \
	} \
	int N##_resize(N *s, int size) \
	{ \
		T *temp; \
		temp = realloc(s->arr, size*N##_sizeof_element); \
		if (!temp) return 0; \
		s->arr = temp; \
		if (size < s->len) s->len = size; \
		s->cap = size; \
		return 1; \
	} \
	N##_iterator N##_iterate(const N *s) \
	{ \
		return -1; \
	} \
	int N##_next(const N *s, N##_iterator *iter) \
	{ \
		if (*iter < 0) { \
			++*iter; \
			return s->len > 0; \
		} \
		if (*iter >= s->len-1) { \
			return 0; \
		} \
		++*iter; \
		return 1; \
	} \
	T N##_get_at(const N *s, N##_iterator iter) \
	{ \
		return s->arr[iter]; \
	} \
	void N##_set_at(N *s, T item, N##_iterator iter) \
	{ \
		s->arr[iter] = item; \
	} \
	int N##_insert_at(N *s, T item, N##_iterator iter) \
	{ \
		return N##_insert(s, item, iter); \
	} \
	T N##_pop_at(N *s, N##_iterator iter) \
	{ \
		return N##_pop(s, iter); \
	} \
	struct N /* to avoid extra semicolon outside of a function */

#endif /* ifndef ALIST_H_INCLUDED */





/* llist.h: a CPP-based template implementation of singly-linked list */

#ifndef LLIST_H_INCLUDED
#define LLIST_H_INCLUDED

//#include <stdlib.h>

#define LLIST_PROTO(T, N) \
	typedef struct N##_pair N##_pair; \
	typedef struct N N; \
	typedef struct N##_iterator N##_iterator; \
	N *N##_new(void); \
	void N##_free(N *s); \
	N##_pair *N##_pair_new(T item); \
	int N##_size(const N *s); \
	int N##_insert(N *s, T item, int pos); \
	T N##_pop(N *s, int pos); \
	T N##_get(const N *s, int pos); \
	void N##_set(N *s, T item, int pos); \
	N##_iterator N##_iterate(const N *s); \
	int N##_next(const N *s, N##_iterator *iter); \
	T N##_get_at(const N *s, N##_iterator iter); \
	void N##_set_at(N *s, T item, N##_iterator iter); \
	int N##_insert_at(N *s, T item, N##_iterator iter); \
	T N##_pop_at(N *s, N##_iterator iter)

#define LLIST(T, N) \
	struct N##_pair { T car; N##_pair *cdr; }; \
	struct N { int len; N##_pair *first; N##_pair *last; }; \
	struct N##_iterator { N##_pair *prev; N##_pair *curr; }; \
	N *N##_new(void) \
	{ \
		N *s; \
		s = malloc(sizeof(struct N)); \
		if (!s) return NULL; \
		s->len = 0; \
		s->first = NULL; \
		s->last = NULL; \
		return s; \
	} \
	void N##_free(N *s) \
	{ \
		N##_pair *p, *temp; \
		temp = NULL; \
		for (p=s->first; p; ) { \
			temp = p; \
			p = p->cdr; \
			free(temp); \
		} \
		free(s); \
	} \
	N##_pair *N##_pair_new(T item) \
	{ \
		N##_pair *p; \
		p = malloc(sizeof(struct N##_pair)); \
		if (!p) return NULL; \
		p->car = item; \
		p->cdr = NULL; \
		return p; \
	} \
	int N##_size(const N *s) \
	{ \
		return s->len; \
	} \
	int N##_insert(N *s, T item, int pos) \
	{ \
		int i; \
		N##_pair *newp, *p; \
		newp = N##_pair_new(item); \
		if (!newp) return 0; \
		if (!s->first) { \
			s->first = s->last = newp; \
			++s->len; \
			return 1; \
		} \
		if (pos < 0 || pos == s->len) { \
			s->last->cdr = newp; \
			s->last = newp; \
			++s->len; \
			return 1; \
		} \
		if (pos == 0) { \
			newp->cdr = s->first; \
			s->first = newp; \
			++s->len; \
			return 1; \
		} \
		for (p=s->first, i=0; i<pos-1 && p; ++i) p=p->cdr; \
		if (!p) { \
			free(newp); \
			return 0; \
		} \
		newp->cdr = p->cdr; \
		p->cdr = newp; \
		++s->len; \
		if (!newp->cdr || p == s->last) { \
			s->last = newp; \
		} \
		return 1; \
	} \
	T N##_pop(N *s, int pos) \
	{ \
		N##_pair *p, *p2; \
		T temp; \
		int i; \
		if (pos == 0) { \
			p = s->first; \
			s->first = p->cdr; \
			temp = p->car; \
			if (!p->cdr) s->last = NULL; \
			free(p); \
			--s->len; \
			return temp; \
		} \
		if (pos < 0) { \
			pos = s->len - 1; \
		} \
		for (p=s->first, i=0; i<pos-1 && p; ++i) p=p->cdr; \
		temp = p->cdr->car; \
		p2 = p->cdr; \
		p->cdr = p2->cdr; \
		if (!p->cdr) { \
			s->last = p; \
		} \
		free(p2); \
		--s->len; \
		return temp; \
	} \
	T N##_get(const N *s, int pos) \
	{ \
		int i; \
		N##_pair *p; \
		if (pos == 0) { \
			return s->first->car; \
		} \
		if (pos < 0) { \
			return s->last->car; \
		} \
		for (p=s->first, i=0; i<pos && p; ++i) p=p->cdr; \
		return p->car; \
	} \
	void N##_set(N *s, T item, int pos) \
	{ \
		int i; \
		N##_pair *p; \
		if (pos == 0) { \
			s->first->car = item; \
		} else if (pos < 0) { \
			s->last->car = item; \
		} else { \
			for (p=s->first, i=0; i<pos && p; ++i) p=p->cdr; \
			p->car = item; \
		} \
	} \
	N##_iterator N##_iterate(const N *s) \
	{ \
		N##_iterator iter = {NULL, NULL}; \
		return iter; \
	} \
	int N##_next(const N *s, N##_iterator *iter) \
	{ \
		if (!iter->curr) { \
			iter->curr = s->first; \
			return 1; \
		} \
		if (!iter->curr->cdr) { \
			return 0; \
		} \
		iter->prev = iter->curr; \
		iter->curr = iter->curr->cdr; \
		return 1; \
	} \
	T N##_get_at(const N *s, N##_iterator iter) \
	{ \
		return iter.curr->car; \
	} \
	void N##_set_at(N *s, T item, N##_iterator iter) \
	{ \
		iter.curr->car = item; \
	} \
	int N##_insert_at(N *s, T item, N##_iterator iter) \
	{ \
		N##_pair *newp; \
		newp = N##_pair_new(item); \
		if (!newp) return 0; \
		++s->len; \
		newp->cdr = iter.curr; \
		if (!iter.prev) { \
			s->first = newp; \
		} else { \
			iter.prev->cdr = newp; \
		} \
		return 1; \
	} \
	T N##_pop_at(N *s, N##_iterator iter) \
	{ \
		T val; \
		--s->len; \
		if (!iter.prev) { \
			s->first = iter.curr->cdr; \
		} else { \
			iter.prev->cdr = iter.curr->cdr; \
		} \
		if (s->last == iter.curr) s->last = iter.prev; \
		val = iter.curr->car; \
		free(iter.curr); \
		return val; \
	} \
	struct N /* to avoid extra semicolon outside of a function */

#endif /* ifndef LLIST_H_INCLUDED */





/*
 * All examples in here can be used with both array and linked lists. To use
 * arraylists instead of linked lists, change LLIST_PROTO and LLIST to
 * ALIST_PROTO and ALIST, respectively.
 *
 * The examples use a list of integers named int_list. The list name and the
 * type of its elements are arbitrary. The name of supplementary types and
 * functions is derived by appending a _name to the supplied name, such as
 * int_list_new (int_list ## _new). A list can contain pointers as well as
 * normal values (primitives, structs, etc).
 */

/* create prototypes for a list of integers, this should be put in a header*/
LLIST_PROTO(int, int_list);

/* create functions for our list, this goes in a .c file */
LLIST(int, int_list);

/* a helper function to print a list and demonstrate iterating */
void print_list(int_list *list)
{
	/* to iterate lists, use an iterator */
	int_list_iterator i;

	/*
	 * int_list_iterator int_list_iterate(int_list *) will give us a new iterator.
	 * In linked list, that's a poiner to a struct containing the value and
	 * pointer to the next such element of the list and in alist, that's an
	 * integer index.
	 *
	 * int int_list_next(int_list *, int_list_iterator *) must be used first to
	 * move the iterator to the first element. It returns 0 when there are no
	 * more elements in the list.
	 */
	for (i = int_list_iterate(list); int_list_next(list, &i); ) {
		/* 
		 * int int_list_get_at(int list *, int_list_iterator) will retrieve
		 * the value in the list stored at the current position of the iterator.
		 *
		 * This is undefined for new iterators that haven't been moved to the first
		 * element with int_list_next yet.
		 *
		 * like int_list_get_at is the iterator equivalent to int_list_get,
		 * int_list_set_at is equivalent to int_list_set.
		 * There are also int_list_insert_after and int_list_pop_after, which
		 * work on the element one position after the iterator's current position.
		 */
		printf("%d ", int_list_get_at(list, i));
	}
	/* 
	 * int int_list_size(int_list *) is the length of the list.
	 * 
	 * In llist, this shouldn't be changed. In alist, this can be decremented to
	 * remove elements from the tail of the list. alists also have list->cap,
	 * which is the current capacity of the array, of which only the first
	 * list->len elements are defined. These fields are not exported by the _PROTO
	 * macros and have to be exported manually.
	 */
	printf("len: %d\n", int_list_size(list));
}

int main(void)
{
	/* a list is always stored as a pointer */
	int_list *list;

	/* int_list *int_list_new(void) allocates and initializes a new list */
	list = int_list_new();

	/*
	 * int_list_insert(int_list *list, int item, int pos) inserts item to
	 * position pos in the list (so that int_list_get(list, pos) == item).
	 * The type of item parameter is the same as the type of list elements.
	 *
	 * A special position -1 means insert to the tail of the list (same as
	 * inserting with position set int_list_size(list)). That is an O(1) operation
	 * in both llist and alist.
	 *
	 * Additionally, inserting to the head of the list (position == 0) is also
	 * O(1) in linked lists. Otherwise, linked list insertions are O(n) in llist
	 * because it's necessary to iterate the list to the position before inserting
	 * and also in alist because all the elements after pos need to be shifted.
	 */
	int_list_insert(list, 20, 0);  /* {20} */
	int_list_insert(list, 40, 1);  /* {20, 40} */
	int_list_insert(list, 30, 1);  /* {20, 30, 40} */
	int_list_insert(list, 50, -1); /* {20, 30, 40, 50} */
	int_list_insert(list, 10, 0);  /* {10, 20, 30, 40, 50} */
	print_list(list);

	/* int int_list_get(int_list *list, int pos) returns the item in list at
	 * position pos (the return value type depends on the type of list elements).
	 *
	 * Just like in insert, value of -1 means the last item.
	 *
	 * In llist, this is an O(n) operation, as the list must be iterated first.
	 */
	printf("list[3] is %d\n", int_list_get(list, 3)); /* 40 */
	printf("list[-1] is %d\n", int_list_get(list, -1)); /* 50 */

	/*
	 * int int_list_pop(int_list *list, int pos) will remove the element at
	 * position pos from list and return it (return value type depends on element
	 * type). The return value from this function can be ignored to just remove
	 * an element.
	 *
	 * Position -1 again means position int_list_size(list).
	 *
	 * In llist, this is an O(n) operation except for pos 0 or -1, since the
	 * list needs to be iterated to pos. In alist, this is also O(n), since all
	 * elements past position pos need to be shifted back (except on position -1
	 * or int_list_size(list), where it's O(1), as it requires only list->len to
	 * be decremented).
	 */
	printf("pop list[2]: %d\n", int_list_pop(list, 2)); /* 30 */
	print_list(list); /* 10 20 40 50 */

	/*
	 * int_list_set(int_list *list, int value, int pos) sets the element in list
	 * at position pos to value (type of the value parameter is the type of list
	 * elements).
	 *
	 * Position -1 is equivalent to position int_list_size(list).
	 *
	 * In llist, this is a O(n) operation except for the first and last elements,
	 * since the list needs to be iterated to that position first.
	 */
	int_list_set(list, 30, 2);
	int_list_set(list, 40, -1);
	print_list(list); /* 10 20 30 40 */

	/* the list can't just be free'd with free(), as it contains other pointers */
	int_list_free(list);

	return 0;
}
