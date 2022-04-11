#ifndef CADTS_HEAP_H
#define CADTS_HEAP_H

//#pragma GCC diagnostic ignored "-Wunused-function"

//#include <stdlib.h>
//#include <assert.h>

/*
##### DEFINITION:

A heap is a binary tree where each node is smaller (or another propriety) than their children. It can be used to retrieve in O(logn) the smallest item.

#define CADTS_HEAP(NAME,STRU,A_CMP_B)
^ Where NAME is the name that the ADT will get and STRU is the datatype of its items.
A_CMP_B is a comparison between a literal "A" and a literal "B", to use the comparison "smaller", it can be "A<B"

##### FUNCTIONS:

NAME *NAME_init(int size)
^ Creates a heap of the given size (values smaller than 1 are set to 1).

void NAME_free(NAME *heap)
^ Liberates the memory requested by the heap.

STRU NAME_peek(NAME *heap)
^ O(1) Get the smaller item.

STRU NAME_poll(NAME *heap)
^ O(logn) Gets the smaller item and deletes it from the heap.

void NAME_add(NAME *heap, STRU val)
^ O(logn) Adds an item to the heap.

int NAME_len(NAME *heap)
^ O(1) Returns the number of items in the heap.

##### VARIABLES:

-- NONE --

#####
*/

#define CADTS_HEAP(NAME,STRU,A_CMP_B) \
\
typedef struct {\
    int len;\
    int size;\
    STRU *items;\
} NAME;\
\
static NAME *NAME##_init(int size){\
    NAME *heap = malloc(sizeof(NAME));\
    heap->len = 0;\
    if(size<1) size = 1;\
    heap->size = size;\
    heap->items = malloc(sizeof(STRU)*heap->size);\
    return heap;\
}\
\
static void NAME##_free(NAME *heap){\
    free(heap->items);\
    free(heap);\
}\
\
static STRU NAME##_peek(NAME *heap){\
    assert(heap->len>0);\
    return heap->items[0];\
}\
static STRU NAME##_poll(NAME *heap){\
    assert(heap->len>0);\
    STRU ret = heap->items[0];\
    heap->len -= 1;\
    heap->items[0] = heap->items[heap->len];\
    /* Heapify down: */\
    int i = 0;\
    int c = 2*i+1;\
    while(c<heap->len){\
        STRU A,B;\
        if(c+1<heap->len){\
            A = heap->items[c+1];\
            B = heap->items[c];\
            if(A_CMP_B) c = c+1;\
        }\
        A = heap->items[i];\
        B = heap->items[c];\
        if(A_CMP_B) break;\
        /**/\
        heap->items[i] = B;\
        heap->items[c] = A;\
        /**/\
        i = c;\
        c = 2*i+1;\
    }\
    return ret;\
}\
\
static void NAME##_add(NAME *heap, STRU val){\
    if(heap->len==heap->size){\
        heap->size *= 2;\
        heap->items = realloc(heap->items,sizeof(STRU)*heap->size);\
    }\
    /**/\
    heap->items[heap->len] = val;\
    heap->len += 1;\
    /* Heapify up: */\
    int i = heap->len-1;\
    int p = (i-1)/2;\
    while(1){\
        if(i<=0) break;\
        STRU A,B;\
        A = heap->items[i];\
        B = heap->items[p];\
        if(!(A_CMP_B)) break;\
        /**/\
        heap->items[i] = B;\
        heap->items[p] = A;\
        /**/\
        i = p;\
        p = (i-1)/2;\
    }\
}\
\
static inline int NAME##_len(NAME *heap){\
    return heap->len;\
}\
\

#endif



#ifndef CADTS_HASHTABLE_H
#define CADTS_HASHTABLE_H

//#pragma GCC diagnostic ignored "-Wunused-function"
//
//#include <stdlib.h>
//#include <string.h>
//#include <assert.h>

/*
##### DEFINITION:

A hashtable is a data structure that maps keys to values, the keys are hashed
to secure fast access to each key value.

#define CADTS_HASHTABLE(NAME,KEY_STRU,VAL_STRU,A_EQL_B,HASH_A)
^ Where NAME is the name that the ADT will get, KEY_STRU is the datatype of the keys and VAL_STRU is the datatype of the values.
A_EQL_B is a comparison for equality from literal "A" and a literal "B", both of type KEY_STRU, e.g. "A==B" if KEY_STRU is a primitive datatype.
HASH_A is an expresion that uses a literal "A" (a key) of type KEY_STRU, and gives its hash, that then is casted to an unsigned int.

NOTE: Its your responsability that if A_EQL_B implies that x==y replacing A by x and B by y, then HASH_A replacing A by x must be equal to HASH_A replacing A by y.

NOTE: Don't hold more than 1073741822 keys.

##### FUNCTIONS:

NAME *NAME_init()
^ Creates a hashtable.

void NAME_free(NAME *htable)
^ Liberates the memory requested by the hashtable.

void NAME_add(NAME *htable, KEY_STRU key, VAL_STRU val)
^ O(1) Adds a key to the hashtable with the given value. If the key already exists, the current value is replaced.

int NAME_has(NAME *htable, KEY_STRU key)
^ O(1) Checks if a given key exists.

VAL_STRU NAME_get(NAME *htable, KEY_STRU key)
^ O(1) Gets the value of a given key.

VAL_STRU NAME_pop(NAME *htable, KEY_STRU key)
^ O(1) Deletes the given key, returning its associated value.

##### ITERATOR FUNCTIONS:

Iterators are designed for traversing the whole hashtable in O(n).

NOTE: Iterators shouldn't be used after their hashtable is freed.

NAME_iter NAME_begin(NAME *htable)
^ Returns an iterator for the hashtable.

int NAME_iter_done(NAME_iter *iter)
^ Checks if the iterator has finish.

void NAME_iter_next(NAME_iter *iter)
^ Moves the iterator forward.

KEY_STRU NAME_iter_key(NAME_iter *iter)
^ Retrieves the key at the current iterator.

VAL_STRU NAME_iter_val(NAME_iter *iter)
^ Retrieves the value at the current iterator.

int NAME_len(NAME *htable)
^ O(1) Returns the number of items in the hashtable.

##### VARIABLES:

-- NONE --

#####
*/

static const unsigned int PRIMELESSP2[] = {
    3,7,13,31,61,127,251,509,1021,2039,4093,8191,16381,32749,65521,131071,
    262139,524287,1048573,2097143,4194301,8388593,16777213,33554393,67108859,
    134217689,268435399,536870909,1073741789,2147483647};


#define CADTS_HASHTABLE(NAME,KEY_STRU,VAL_STRU,A_EQL_B,HASH_A) \
\
typedef struct NAME##_node NAME##_node;\
struct NAME##_node {\
    NAME##_node *next;\
    KEY_STRU key;\
    VAL_STRU val;\
};\
\
typedef struct {\
    int n_modifications;\
    int len;\
    int sizei;\
    NAME##_node **slots;\
} NAME;\
\
NAME *NAME##_init(){\
    NAME *htable = malloc(sizeof(NAME));\
    htable->n_modifications = 0;\
    htable->len = 0;\
    htable->sizei = 0;\
    htable->slots = malloc(sizeof(NAME##_node *)*PRIMELESSP2[htable->sizei]);\
    for(int i=0;i<PRIMELESSP2[htable->sizei];i++) htable->slots[i] = NULL;\
    return htable;\
}\
\
void NAME##_free(NAME *htable){\
    for(int i=0;i<PRIMELESSP2[htable->sizei];i++){\
        NAME##_node *ptr = htable->slots[i];\
        while(ptr!=NULL){\
            NAME##_node *ptrc = ptr;\
            ptr = ptr->next;\
            free(ptrc);\
        }\
    }\
    free(htable->slots);\
    free(htable);\
}\
\
void NAME##_add(NAME *htable, KEY_STRU key, VAL_STRU val){\
    /* Check for presence of the current key */ \
    KEY_STRU A = key;\
    unsigned int hash = ((unsigned int)(HASH_A));\
    unsigned int slot = hash%PRIMELESSP2[htable->sizei];\
    NAME##_node *ptr = htable->slots[slot];\
    while(ptr!=NULL){\
        NAME##_node *ptrc = ptr;\
        ptr = ptr->next;\
        KEY_STRU B = ptrc->key;\
        /* If key was found */ \
        if(A_EQL_B){\
            ptrc->val = val;\
            return;\
        }\
    }\
    /* Update counters */ \
    htable->n_modifications += 1;\
    htable->len += 1;\
    /* Realloc all the nodes in more space */ \
    if(htable->len>=PRIMELESSP2[htable->sizei]/2){\
        NAME##_node **neo_slots = malloc(sizeof(NAME##_node *)*PRIMELESSP2[htable->sizei+1]);\
        for(int i=0;i<PRIMELESSP2[htable->sizei+1];i++) neo_slots[i] = NULL;\
        for(int i=0;i<PRIMELESSP2[htable->sizei];i++){\
            NAME##_node *ptr = htable->slots[i];\
            while(ptr!=NULL){\
                NAME##_node *ptrc = ptr;\
                ptr = ptr->next;\
                /* Add this node in the neo_slots */ \
                KEY_STRU A = ptrc->key;\
                unsigned int hash = ((unsigned int)(HASH_A));\
                unsigned int cslot = hash%PRIMELESSP2[htable->sizei+1];\
                ptrc->next = neo_slots[cslot];\
                neo_slots[cslot] = ptrc;\
            }\
        }\
        free(htable->slots);\
        htable->slots = neo_slots;\
        htable->sizei += 1;\
        /* Recompute slot of the hash */ \
        slot = hash%PRIMELESSP2[htable->sizei];\
    }\
    /* Create and add new node */ \
    NAME##_node *node = malloc(sizeof(NAME##_node));\
    node->key = key;\
    node->val = val;\
    node->next = htable->slots[slot];\
    htable->slots[slot] = node;\
}\
\
int NAME##_has(NAME *htable, KEY_STRU key){\
    /* Check for presence of the current key */ \
    KEY_STRU A = key;\
    unsigned int hash = ((unsigned int)(HASH_A));\
    unsigned int slot = hash%PRIMELESSP2[htable->sizei];\
    NAME##_node *ptr = htable->slots[slot];\
    while(ptr!=NULL){\
        NAME##_node *ptrc = ptr;\
        ptr = ptr->next;\
        KEY_STRU B = ptrc->key;\
        /* If key was found */ \
        if(A_EQL_B) return 1; \
    }\
    return 0;\
}\
\
VAL_STRU NAME##_get(NAME *htable, KEY_STRU key){\
    /* Check for presence of the current key */ \
    KEY_STRU A = key;\
    unsigned int hash = ((unsigned int)(HASH_A));\
    unsigned int slot = hash%PRIMELESSP2[htable->sizei];\
    NAME##_node *ptr = htable->slots[slot];\
    while(ptr!=NULL){\
        NAME##_node *ptrc = ptr;\
        ptr = ptr->next;\
        KEY_STRU B = ptrc->key;\
        /* Key was found */ \
        if(A_EQL_B){\
            return ptrc->val;\
        }\
    }\
    /* Key was not found and should have been, return 0-inited struct for deterministic behaviour if assertion is ignored. */ \
    assert(!"Key exists.");\
    VAL_STRU dummy;\
    memset(&dummy,0,sizeof(VAL_STRU));\
    return dummy;\
}\
\
VAL_STRU NAME##_pop(NAME *htable, KEY_STRU key){\
    /* Check for presence of the current key */ \
    KEY_STRU A = key;\
    unsigned int hash = ((unsigned int)(HASH_A));\
    unsigned int slot = hash%PRIMELESSP2[htable->sizei];\
    NAME##_node **preptr = &htable->slots[slot];\
    while(*preptr!=NULL){\
        NAME##_node *ptrc = *preptr;\
        KEY_STRU B = ptrc->key;\
        /* If key was found */ \
        if(A_EQL_B){\
            /* Update counters */ \
            htable->n_modifications += 1;\
            htable->len -= 1;\
            /* Make previous pointer point to the next node */ \
            *preptr = ptrc->next;\
            VAL_STRU val = ptrc->val;\
            free(ptrc);\
            /* Realloc all the nodes in less space */ \
            if(htable->len<PRIMELESSP2[htable->sizei]/4){\
                NAME##_node **neo_slots = malloc(sizeof(NAME##_node *)*PRIMELESSP2[htable->sizei-1]);\
                for(int i=0;i<PRIMELESSP2[htable->sizei-1];i++) neo_slots[i] = NULL;\
                for(int i=0;i<PRIMELESSP2[htable->sizei];i++){\
                    NAME##_node *ptr = htable->slots[i];\
                    while(ptr!=NULL){\
                        NAME##_node *ptrc = ptr;\
                        ptr = ptr->next;\
                        /* Add this node in the neo_slots */ \
                        KEY_STRU A = ptrc->key;\
                        unsigned int hash = ((unsigned int)(HASH_A));\
                        unsigned int cslot = hash%PRIMELESSP2[htable->sizei-1];\
                        ptrc->next = neo_slots[cslot];\
                        neo_slots[cslot] = ptrc;\
                    }\
                }\
                free(htable->slots);\
                htable->slots = neo_slots;\
                htable->sizei -= 1;\
            }\
            /* Return val */ \
            return val;\
        }\
        /* Advance pre pointer */ \
        preptr = &ptrc->next;\
    }\
    /* Key was not found and should have been, return 0-inited struct for deterministic behaviour if assertion is ignored. */ \
    assert(!"Key exists.");\
    VAL_STRU dummy;\
    memset(&dummy,0,sizeof(VAL_STRU));\
    return dummy;\
}\
\
typedef struct {\
    NAME *origin;\
    int n_modifications;\
    int slot;\
    NAME##_node *ptrc;\
} NAME##_iter;\
\
NAME##_iter NAME##_begin(NAME *htable){\
    NAME##_iter iter;\
    iter.origin = htable;\
    iter.n_modifications = htable->n_modifications;\
    iter.slot = PRIMELESSP2[iter.origin->sizei];\
    for(int i=0;i<PRIMELESSP2[iter.origin->sizei];i++){\
        if(iter.origin->slots[i]!=NULL){\
            iter.ptrc = iter.origin->slots[i];\
            iter.slot = i;\
            break;\
        }\
    }\
    return iter;\
}\
\
void NAME##_iter_next(NAME##_iter *iter){\
    if(iter->n_modifications!=iter->origin->n_modifications){\
        assert(!"Hashtable wasn't modified.");\
    }\
    /* Check if iterator is already done */ \
    if(iter->slot==PRIMELESSP2[iter->origin->sizei]) return;\
    /* Move iterator forward until reach end or finding another node */ \
    iter->ptrc = iter->ptrc->next;\
    while(iter->ptrc==NULL){\
        iter->slot += 1;\
        if(iter->slot==PRIMELESSP2[iter->origin->sizei]) break; \
        iter->ptrc = iter->origin->slots[iter->slot];\
    }\
}\
\
int NAME##_iter_done(NAME##_iter *iter){\
    if(iter->n_modifications!=iter->origin->n_modifications){\
        assert(!"Hashtable wasn't modified.");\
    }\
    return iter->slot==PRIMELESSP2[iter->origin->sizei];\
}\
\
KEY_STRU NAME##_iter_key(NAME##_iter *iter){\
    if(iter->n_modifications!=iter->origin->n_modifications){\
        assert(!"Hashtable wasn't modified.");\
    }\
    assert(!NAME##_iter_done(iter));\
    return iter->ptrc->key;\
}\
\
VAL_STRU NAME##_iter_val(NAME##_iter *iter){\
    if(iter->n_modifications!=iter->origin->n_modifications){\
        assert(!"Hashtable wasn't modified.");\
    }\
    assert(!NAME##_iter_done(iter));\
    return iter->ptrc->val;\
}\
\
static inline int NAME##_len(NAME *htable){\
    return htable->len;\
}\
\

#endif


#ifndef CADTS_VECTOR_H
#define CADTS_VECTOR_H

//#pragma GCC diagnostic ignored "-Wunused-function"
//
//#include <stdlib.h>
//#include <assert.h>

/*
##### DEFINITION:

A vector is just an array that resizes once its capacity is not enough to store new values.

#define CADTS_VECTOR(NAME,STRU)
^ Where NAME is the name that the ADT will get and STRU is the datatype of its items.

##### FUNCTIONS:

NAME *NAME_init(int size)
^ Creates a vector of the given size (values smaller than 1 are set to 1).

void NAME_free(NAME *vect)
^ Liberates the memory requested by the vector.

void NAME_endadd(NAME *vect, STRU stru)
^ O(1) Adds an item at the end of the vector. Increasing its length by 1.

STRU NAME_endpop(NAME *vect)
^ O(1) Removes the last item from the vector, returning its value.

STRU NAME_endtop(NAME *vect)
^ O(1) Returns the last item of the vector.

void NAME_add(NAME *vect, int p, STRU stru)
^ O(n-p) Adds an item at position p.

STRU NAME_pop(NAME *vect, int p)
^ O(n-p) Deletes the item on position p, returning its value.

int NAME_len(NAME *vect)
^ O(1) Returns the number of items in the vector.

STRU NAME_item(NAME *vect, int p)
^ O(1) Returns the item at the position p.

##### VARIABLES:

STRU vect->items[k]
^ Access the struct at position k.

#####
*/

#define CADTS_VECTOR(NAME,STRU) \
\
typedef struct {\
    int len;\
    int size;\
    STRU *items;\
} NAME;\
\
static NAME *NAME##_init(int size){\
    NAME *vect = malloc(sizeof(NAME));\
    vect->len = 0;\
    if(size<1) size = 1;\
    vect->size = size;\
    vect->items = (STRU *) malloc(sizeof(STRU)*vect->size);\
    return vect;\
}\
\
static void NAME##_free(NAME *vect){\
    free(vect->items);\
    free(vect);\
}\
\
static void NAME##_endadd(NAME *vect, STRU stru){\
    if(vect->len==vect->size){\
        vect->size *= 2;\
        vect->items = (STRU *) realloc(vect->items,sizeof(STRU)*vect->size);\
    }\
    vect->items[vect->len] = stru;\
    vect->len += 1;\
}\
\
static STRU NAME##_endpop(NAME *vect){\
    assert(vect->len>0);\
    vect->len -= 1;\
    return vect->items[vect->len];\
}\
\
static STRU NAME##_endtop(NAME *vect){\
    assert(vect->len>0);\
    return vect->items[vect->len - 1];\
}\
\
static void NAME##_add(NAME *vect, int p, STRU stru){\
    assert(p>=0 && p<=vect->len);\
    if(vect->len==vect->size){\
        vect->size *= 2;\
        vect->items = (STRU *) realloc(vect->items,sizeof(STRU)*vect->size);\
    }\
    for(int i=vect->len-1; p<=i; i--){\
        vect->items[i+1] = vect->items[i];\
    }\
    vect->len += 1;\
    vect->items[p] = stru;\
}\
\
static STRU NAME##_pop(NAME *vect, int p){\
    assert(p>=0 && p<vect->len);\
    STRU ret = vect->items[p];\
    vect->len -= 1;\
    for(int i=p; i<vect->len; i++){\
        vect->items[i] = vect->items[i+1];\
    }\
    return ret;\
}\
\
static inline int NAME##_len(NAME *vect){\
    return vect->len;\
}\
\
static STRU NAME##_item(NAME *vect, int p){\
    assert(p>=0 && p<vect->len);\
    return vect->items[p];\
}\
\

#endif


//#include <stdio.h>
//
//#include "cadts_vector.h"
//#include "cadts_heap.h"
//#include "cadts_hashtable.h"

// Create floatvec, a struct that is a vector of floats:
CADTS_VECTOR(floatvec,float)

// Create intheap, a struct that is a heap of ints
// the third argument should be a comparison of "A" and "B", which are ints in this case.
CADTS_HEAP(intheap,int,A<B)

// Create loloctable, a struct with unsigned long long's as keys and chars as values
// the third argument checks for equality between "A" and "B".
// the fourth argument hashs the key "A".
CADTS_HASHTABLE(loloctable,unsigned long long,char,A==B,(A>>32)^(A))


int cmp_floats(const void *a, const void *b){
    return (*(float*)a-*(float*)b);
}


int main(int argc, char const *argv[]){
    //
    //
    printf("VECTOR:\n");
    floatvec *fvec = floatvec_init(0);
    for(int i=0;i<20;i++){
        floatvec_endadd(fvec,20-3*i-2*(i%2));
    }
    for(int i=0;i<10;i++){
        floatvec_endpop(fvec);
    }
    // Print last element
    printf("%f\n", floatvec_endtop(fvec));
    // Print vector
    for(int i=0;i< floatvec_len(fvec);i++){
        // Access by "items" array, this can only be done with vectors.
        printf("%f ",fvec->items[i]);
    }
    printf("\n");
    // Sort vector acceding by array
    qsort(fvec->items,floatvec_len(fvec),sizeof(float),cmp_floats);
    // Print vector
    for(int i=0;i< floatvec_len(fvec);i++){
        // Access by "items" array, this can only be done with vectors.
        printf("%f ",fvec->items[i]);
    }
    printf("\n");
    // Free vector
    floatvec_free(fvec);
    //
    printf("HEAP:\n");
    intheap *heap = intheap_init(0);
    intheap_add(heap,5);
    intheap_add(heap,7);
    intheap_add(heap,1);
    intheap_add(heap,4);
    intheap_add(heap,3);
    for(int i=0;i<3;i++){
        printf("heap lowest: %d\n",intheap_poll(heap));
    }
    intheap_add(heap,2);
    intheap_add(heap,10);
    for(int i=0;i<4;i++){
        printf("heap lowest: %d\n",intheap_poll(heap));
    }
    intheap_free(heap);
    //
    //
    printf("HASHTABLE:\n");
    loloctable *htable = loloctable_init();
    loloctable_add(htable,600000,'B');
    loloctable_add(htable,600002,'O');
    loloctable_add(htable,600003,'O');
    loloctable_add(htable,600004,'K');
    loloctable_add(htable,600005,' ');
    loloctable_add(htable,600006,'K');
    loloctable_add(htable,600007,'E');
    loloctable_add(htable,600008,'E');
    loloctable_add(htable,600009,'P');
    loloctable_add(htable,600010,'E');
    loloctable_add(htable,600011,'R');
    char val = loloctable_pop(htable,600004);
    printf("poped: '%c'\n",val);
    // Test iterator:
    for(loloctable_iter it=loloctable_begin(htable); !loloctable_iter_done(&it); loloctable_iter_next(&it)){
        printf("KEY '%llu' VAL '%c'\n",loloctable_iter_key(&it),loloctable_iter_val(&it));
    }
    // Delete something
    loloctable_pop(htable,600002);
    loloctable_pop(htable,600005);
    loloctable_pop(htable,600006);
    loloctable_pop(htable,600007);
    loloctable_pop(htable,600009);
    loloctable_pop(htable,600010);
    printf("poped several\n");
    // Iterate again:
    for(loloctable_iter it=loloctable_begin(htable); !loloctable_iter_done(&it); loloctable_iter_next(&it)){
        printf("KEY '%llu' VAL '%c'\n",loloctable_iter_key(&it),loloctable_iter_val(&it));
    }

    printf("poped 600004: '%c'\n",val);
    loloctable_free(htable);
    //
    //
    return 0;
}
