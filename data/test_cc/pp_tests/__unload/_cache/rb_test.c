/*-
 *******************************************************************************
 *
 * cpp macro implementation of left-leaning 2-3 red-black trees.  Parent
 * pointers are not used, and color bits are stored in the least significant
 * bit of right-child pointers (if RB_COMPACT is defined), thus making node
 * linkage as compact as is possible for red-black trees.
 *
 * Usage:
 *
 *   #include <stdint.h>
 *   #include <stdbool.h>
 *   #define NDEBUG // (Optional, see assert(3).)
 *   #include <assert.h>
 *   #define RB_COMPACT // (Optional, embed color bits in right-child pointers.)
 *   #include <rb.h>
 *   ...
 *
 *******************************************************************************
 */

#ifndef RB_H_
#define	RB_H_

#ifdef RB_COMPACT
/* Node structure. */
#define	rb_node(a_type)							\
struct {								\
    a_type *rbn_left;							\
    a_type *rbn_right_red;						\
}
#else
#define	rb_node(a_type)							\
struct {								\
    a_type *rbn_left;							\
    a_type *rbn_right;							\
    bool rbn_red;							\
}
#endif

/* Root structure. */
#define	rbt(a_type)							\
struct {								\
    a_type *rbt_root;							\
    a_type rbt_nil;							\
}

/* Left accessors. */
#define	rbtn_left_get(a_type, a_field, a_node)				\
    ((a_node)->a_field.rbn_left)
#define	rbtn_left_set(a_type, a_field, a_node, a_left) do {		\
    (a_node)->a_field.rbn_left = a_left;				\
} while (0)

#ifdef RB_COMPACT
/* Right accessors. */
#define	rbtn_right_get(a_type, a_field, a_node)				\
    ((a_type *) (((intptr_t) (a_node)->a_field.rbn_right_red)		\
      & ((ssize_t)-2)))
#define	rbtn_right_set(a_type, a_field, a_node, a_right) do {		\
    (a_node)->a_field.rbn_right_red = (a_type *) (((uintptr_t) a_right)	\
      | (((uintptr_t) (a_node)->a_field.rbn_right_red) & ((size_t)1)));	\
} while (0)

/* Color accessors. */
#define	rbtn_red_get(a_type, a_field, a_node)				\
    ((bool) (((uintptr_t) (a_node)->a_field.rbn_right_red)		\
      & ((size_t)1)))
#define	rbtn_color_set(a_type, a_field, a_node, a_red) do {		\
    (a_node)->a_field.rbn_right_red = (a_type *) ((((intptr_t)		\
      (a_node)->a_field.rbn_right_red) & ((ssize_t)-2))			\
      | ((ssize_t)a_red));						\
} while (0)
#define	rbtn_red_set(a_type, a_field, a_node) do {			\
    (a_node)->a_field.rbn_right_red = (a_type *) (((uintptr_t)		\
      (a_node)->a_field.rbn_right_red) | ((size_t)1));			\
} while (0)
#define	rbtn_black_set(a_type, a_field, a_node) do {			\
    (a_node)->a_field.rbn_right_red = (a_type *) (((intptr_t)		\
      (a_node)->a_field.rbn_right_red) & ((ssize_t)-2));		\
} while (0)
#else
/* Right accessors. */
#define	rbtn_right_get(a_type, a_field, a_node)				\
    ((a_node)->a_field.rbn_right)
#define	rbtn_right_set(a_type, a_field, a_node, a_right) do {		\
    (a_node)->a_field.rbn_right = a_right;				\
} while (0)

/* Color accessors. */
#define	rbtn_red_get(a_type, a_field, a_node)				\
    ((a_node)->a_field.rbn_red)
#define	rbtn_color_set(a_type, a_field, a_node, a_red) do {		\
    (a_node)->a_field.rbn_red = (a_red);				\
} while (0)
#define	rbtn_red_set(a_type, a_field, a_node) do {			\
    (a_node)->a_field.rbn_red = true;					\
} while (0)
#define	rbtn_black_set(a_type, a_field, a_node) do {			\
    (a_node)->a_field.rbn_red = false;					\
} while (0)
#endif

/* Node initializer. */
#define	rbt_node_new(a_type, a_field, a_rbt, a_node) do {		\
    rbtn_left_set(a_type, a_field, (a_node), &(a_rbt)->rbt_nil);	\
    rbtn_right_set(a_type, a_field, (a_node), &(a_rbt)->rbt_nil);	\
    rbtn_red_set(a_type, a_field, (a_node));				\
} while (0)

/* Tree initializer. */
#define	rb_new(a_type, a_field, a_rbt) do {				\
    (a_rbt)->rbt_root = &(a_rbt)->rbt_nil;				\
    rbt_node_new(a_type, a_field, a_rbt, &(a_rbt)->rbt_nil);		\
    rbtn_black_set(a_type, a_field, &(a_rbt)->rbt_nil);			\
} while (0)

/* Internal utility macros. */
#define	rbtn_first(a_type, a_field, a_rbt, a_root, r_node) do {		\
    (r_node) = (a_root);						\
    if ((r_node) != &(a_rbt)->rbt_nil) {				\
	for (;								\
	  rbtn_left_get(a_type, a_field, (r_node)) != &(a_rbt)->rbt_nil;\
	  (r_node) = rbtn_left_get(a_type, a_field, (r_node))) {	\
	}								\
    }									\
} while (0)

#define	rbtn_last(a_type, a_field, a_rbt, a_root, r_node) do {		\
    (r_node) = (a_root);						\
    if ((r_node) != &(a_rbt)->rbt_nil) {				\
	for (; rbtn_right_get(a_type, a_field, (r_node)) !=		\
	  &(a_rbt)->rbt_nil; (r_node) = rbtn_right_get(a_type, a_field,	\
	  (r_node))) {							\
	}								\
    }									\
} while (0)

#define	rbtn_rotate_left(a_type, a_field, a_node, r_node) do {		\
    (r_node) = rbtn_right_get(a_type, a_field, (a_node));		\
    rbtn_right_set(a_type, a_field, (a_node),				\
      rbtn_left_get(a_type, a_field, (r_node)));			\
    rbtn_left_set(a_type, a_field, (r_node), (a_node));			\
} while (0)

#define	rbtn_rotate_right(a_type, a_field, a_node, r_node) do {		\
    (r_node) = rbtn_left_get(a_type, a_field, (a_node));		\
    rbtn_left_set(a_type, a_field, (a_node),				\
      rbtn_right_get(a_type, a_field, (r_node)));			\
    rbtn_right_set(a_type, a_field, (r_node), (a_node));		\
} while (0)

/*
 * The rb_proto() macro generates function prototypes that correspond to the
 * functions generated by an equivalently parameterized call to rb_gen().
 */

#define	rb_proto(a_attr, a_prefix, a_rbt_type, a_type)			\
a_attr void								\
a_prefix##new(a_rbt_type *rbtree);					\
a_attr a_type *								\
a_prefix##first(a_rbt_type *rbtree);					\
a_attr a_type *								\
a_prefix##last(a_rbt_type *rbtree);					\
a_attr a_type *								\
a_prefix##next(a_rbt_type *rbtree, a_type *node);			\
a_attr a_type *								\
a_prefix##prev(a_rbt_type *rbtree, a_type *node);			\
a_attr a_type *								\
a_prefix##search(a_rbt_type *rbtree, a_type *key);			\
a_attr a_type *								\
a_prefix##nsearch(a_rbt_type *rbtree, a_type *key);			\
a_attr a_type *								\
a_prefix##psearch(a_rbt_type *rbtree, a_type *key);			\
a_attr void								\
a_prefix##insert(a_rbt_type *rbtree, a_type *node);			\
a_attr void								\
a_prefix##remove(a_rbt_type *rbtree, a_type *node);			\
a_attr a_type *								\
a_prefix##iter(a_rbt_type *rbtree, a_type *start, a_type *(*cb)(	\
  a_rbt_type *, a_type *, void *), void *arg);				\
a_attr a_type *								\
a_prefix##reverse_iter(a_rbt_type *rbtree, a_type *start,		\
  a_type *(*cb)(a_rbt_type *, a_type *, void *), void *arg);

/*
 * The rb_gen() macro generates a type-specific red-black tree implementation,
 * based on the above cpp macros.
 *
 * Arguments:
 *
 *   a_attr    : Function attribute for generated functions (ex: static).
 *   a_prefix  : Prefix for generated functions (ex: ex_).
 *   a_rb_type : Type for red-black tree data structure (ex: ex_t).
 *   a_type    : Type for red-black tree node data structure (ex: ex_node_t).
 *   a_field   : Name of red-black tree node linkage (ex: ex_link).
 *   a_cmp     : Node comparison function name, with the following prototype:
 *                 int (a_cmp *)(a_type *a_node, a_type *a_other);
 *                                       ^^^^^^
 *                                    or a_key
 *               Interpretation of comparision function return values:
 *                 -1 : a_node <  a_other
 *                  0 : a_node == a_other
 *                  1 : a_node >  a_other
 *               In all cases, the a_node or a_key macro argument is the first
 *               argument to the comparison function, which makes it possible
 *               to write comparison functions that treat the first argument
 *               specially.
 *
 * Assuming the following setup:
 *
 *   typedef struct ex_node_s ex_node_t;
 *   struct ex_node_s {
 *       rb_node(ex_node_t) ex_link;
 *   };
 *   typedef rbt(ex_node_t) ex_t;
 *   rb_gen(static, ex_, ex_t, ex_node_t, ex_link, ex_cmp)
 *
 * The following API is generated:
 *
 *   static void
 *   ex_new(ex_t *tree);
 *       Description: Initialize a red-black tree structure.
 *       Args:
 *         tree: Pointer to an uninitialized red-black tree object.
 *
 *   static ex_node_t *
 *   ex_first(ex_t *tree);
 *   static ex_node_t *
 *   ex_last(ex_t *tree);
 *       Description: Get the first/last node in tree.
 *       Args:
 *         tree: Pointer to an initialized red-black tree object.
 *       Ret: First/last node in tree, or NULL if tree is empty.
 *
 *   static ex_node_t *
 *   ex_next(ex_t *tree, ex_node_t *node);
 *   static ex_node_t *
 *   ex_prev(ex_t *tree, ex_node_t *node);
 *       Description: Get node's successor/predecessor.
 *       Args:
 *         tree: Pointer to an initialized red-black tree object.
 *         node: A node in tree.
 *       Ret: node's successor/predecessor in tree, or NULL if node is
 *            last/first.
 *
 *   static ex_node_t *
 *   ex_search(ex_t *tree, ex_node_t *key);
 *       Description: Search for node that matches key.
 *       Args:
 *         tree: Pointer to an initialized red-black tree object.
 *         key : Search key.
 *       Ret: Node in tree that matches key, or NULL if no match.
 *
 *   static ex_node_t *
 *   ex_nsearch(ex_t *tree, ex_node_t *key);
 *   static ex_node_t *
 *   ex_psearch(ex_t *tree, ex_node_t *key);
 *       Description: Search for node that matches key.  If no match is found,
 *                    return what would be key's successor/predecessor, were
 *                    key in tree.
 *       Args:
 *         tree: Pointer to an initialized red-black tree object.
 *         key : Search key.
 *       Ret: Node in tree that matches key, or if no match, hypothetical node's
 *            successor/predecessor (NULL if no successor/predecessor).
 *
 *   static void
 *   ex_insert(ex_t *tree, ex_node_t *node);
 *       Description: Insert node into tree.
 *       Args:
 *         tree: Pointer to an initialized red-black tree object.
 *         node: Node to be inserted into tree.
 *
 *   static void
 *   ex_remove(ex_t *tree, ex_node_t *node);
 *       Description: Remove node from tree.
 *       Args:
 *         tree: Pointer to an initialized red-black tree object.
 *         node: Node in tree to be removed.
 *
 *   static ex_node_t *
 *   ex_iter(ex_t *tree, ex_node_t *start, ex_node_t *(*cb)(ex_t *,
 *     ex_node_t *, void *), void *arg);
 *   static ex_node_t *
 *   ex_reverse_iter(ex_t *tree, ex_node_t *start, ex_node *(*cb)(ex_t *,
 *     ex_node_t *, void *), void *arg);
 *       Description: Iterate forward/backward over tree, starting at node.  If
 *                    tree is modified, iteration must be immediately
 *                    terminated by the callback function that causes the
 *                    modification.
 *       Args:
 *         tree : Pointer to an initialized red-black tree object.
 *         start: Node at which to start iteration, or NULL to start at
 *                first/last node.
 *         cb   : Callback function, which is called for each node during
 *                iteration.  Under normal circumstances the callback function
 *                should return NULL, which causes iteration to continue.  If a
 *                callback function returns non-NULL, iteration is immediately
 *                terminated and the non-NULL return value is returned by the
 *                iterator.  This is useful for re-starting iteration after
 *                modifying tree.
 *         arg  : Opaque pointer passed to cb().
 *       Ret: NULL if iteration completed, or the non-NULL callback return value
 *            that caused termination of the iteration.
 */
#define	rb_gen(a_attr, a_prefix, a_rbt_type, a_type, a_field, a_cmp)	\
a_attr void								\
a_prefix##new(a_rbt_type *rbtree) {					\
    rb_new(a_type, a_field, rbtree);					\
}									\
a_attr a_type *								\
a_prefix##first(a_rbt_type *rbtree) {					\
    a_type *ret;							\
    rbtn_first(a_type, a_field, rbtree, rbtree->rbt_root, ret);		\
    if (ret == &rbtree->rbt_nil) {					\
	ret = NULL;							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##last(a_rbt_type *rbtree) {					\
    a_type *ret;							\
    rbtn_last(a_type, a_field, rbtree, rbtree->rbt_root, ret);		\
    if (ret == &rbtree->rbt_nil) {					\
	ret = NULL;							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##next(a_rbt_type *rbtree, a_type *node) {			\
    a_type *ret;							\
    if (rbtn_right_get(a_type, a_field, node) != &rbtree->rbt_nil) {	\
	rbtn_first(a_type, a_field, rbtree, rbtn_right_get(a_type,	\
	  a_field, node), ret);						\
    } else {								\
	a_type *tnode = rbtree->rbt_root;				\
	assert(tnode != &rbtree->rbt_nil);				\
	ret = &rbtree->rbt_nil;						\
	while (true) {							\
	    int cmp = (a_cmp)(node, tnode);				\
	    if (cmp < 0) {						\
		ret = tnode;						\
		tnode = rbtn_left_get(a_type, a_field, tnode);		\
	    } else if (cmp > 0) {					\
		tnode = rbtn_right_get(a_type, a_field, tnode);		\
	    } else {							\
		break;							\
	    }								\
	    assert(tnode != &rbtree->rbt_nil);				\
	}								\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = (NULL);							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##prev(a_rbt_type *rbtree, a_type *node) {			\
    a_type *ret;							\
    if (rbtn_left_get(a_type, a_field, node) != &rbtree->rbt_nil) {	\
	rbtn_last(a_type, a_field, rbtree, rbtn_left_get(a_type,	\
	  a_field, node), ret);						\
    } else {								\
	a_type *tnode = rbtree->rbt_root;				\
	assert(tnode != &rbtree->rbt_nil);				\
	ret = &rbtree->rbt_nil;						\
	while (true) {							\
	    int cmp = (a_cmp)(node, tnode);				\
	    if (cmp < 0) {						\
		tnode = rbtn_left_get(a_type, a_field, tnode);		\
	    } else if (cmp > 0) {					\
		ret = tnode;						\
		tnode = rbtn_right_get(a_type, a_field, tnode);		\
	    } else {							\
		break;							\
	    }								\
	    assert(tnode != &rbtree->rbt_nil);				\
	}								\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = (NULL);							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##search(a_rbt_type *rbtree, a_type *key) {			\
    a_type *ret;							\
    int cmp;								\
    ret = rbtree->rbt_root;						\
    while (ret != &rbtree->rbt_nil					\
      && (cmp = (a_cmp)(key, ret)) != 0) {				\
	if (cmp < 0) {							\
	    ret = rbtn_left_get(a_type, a_field, ret);			\
	} else {							\
	    ret = rbtn_right_get(a_type, a_field, ret);			\
	}								\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = (NULL);							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##nsearch(a_rbt_type *rbtree, a_type *key) {			\
    a_type *ret;							\
    a_type *tnode = rbtree->rbt_root;					\
    ret = &rbtree->rbt_nil;						\
    while (tnode != &rbtree->rbt_nil) {					\
	int cmp = (a_cmp)(key, tnode);					\
	if (cmp < 0) {							\
	    ret = tnode;						\
	    tnode = rbtn_left_get(a_type, a_field, tnode);		\
	} else if (cmp > 0) {						\
	    tnode = rbtn_right_get(a_type, a_field, tnode);		\
	} else {							\
	    ret = tnode;						\
	    break;							\
	}								\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = (NULL);							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##psearch(a_rbt_type *rbtree, a_type *key) {			\
    a_type *ret;							\
    a_type *tnode = rbtree->rbt_root;					\
    ret = &rbtree->rbt_nil;						\
    while (tnode != &rbtree->rbt_nil) {					\
	int cmp = (a_cmp)(key, tnode);					\
	if (cmp < 0) {							\
	    tnode = rbtn_left_get(a_type, a_field, tnode);		\
	} else if (cmp > 0) {						\
	    ret = tnode;						\
	    tnode = rbtn_right_get(a_type, a_field, tnode);		\
	} else {							\
	    ret = tnode;						\
	    break;							\
	}								\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = (NULL);							\
    }									\
    return (ret);							\
}									\
a_attr void								\
a_prefix##insert(a_rbt_type *rbtree, a_type *node) {			\
    struct {								\
	a_type *node;							\
	int cmp;							\
    } path[sizeof(void *) << 4], *pathp;				\
    rbt_node_new(a_type, a_field, rbtree, node);			\
    /* Wind. */								\
    path->node = rbtree->rbt_root;					\
    for (pathp = path; pathp->node != &rbtree->rbt_nil; pathp++) {	\
	int cmp = pathp->cmp = a_cmp(node, pathp->node);		\
	assert(cmp != 0);						\
	if (cmp < 0) {							\
	    pathp[1].node = rbtn_left_get(a_type, a_field,		\
	      pathp->node);						\
	} else {							\
	    pathp[1].node = rbtn_right_get(a_type, a_field,		\
	      pathp->node);						\
	}								\
    }									\
    pathp->node = node;							\
    /* Unwind. */							\
    for (pathp--; (uintptr_t)pathp >= (uintptr_t)path; pathp--) {	\
	a_type *cnode = pathp->node;					\
	if (pathp->cmp < 0) {						\
	    a_type *left = pathp[1].node;				\
	    rbtn_left_set(a_type, a_field, cnode, left);		\
	    if (rbtn_red_get(a_type, a_field, left)) {			\
		a_type *leftleft = rbtn_left_get(a_type, a_field, left);\
		if (rbtn_red_get(a_type, a_field, leftleft)) {		\
		    /* Fix up 4-node. */				\
		    a_type *tnode;					\
		    rbtn_black_set(a_type, a_field, leftleft);		\
		    rbtn_rotate_right(a_type, a_field, cnode, tnode);	\
		    cnode = tnode;					\
		}							\
	    } else {							\
		return;							\
	    }								\
	} else {							\
	    a_type *right = pathp[1].node;				\
	    rbtn_right_set(a_type, a_field, cnode, right);		\
	    if (rbtn_red_get(a_type, a_field, right)) {			\
		a_type *left = rbtn_left_get(a_type, a_field, cnode);	\
		if (rbtn_red_get(a_type, a_field, left)) {		\
		    /* Split 4-node. */					\
		    rbtn_black_set(a_type, a_field, left);		\
		    rbtn_black_set(a_type, a_field, right);		\
		    rbtn_red_set(a_type, a_field, cnode);		\
		} else {						\
		    /* Lean left. */					\
		    a_type *tnode;					\
		    bool tred = rbtn_red_get(a_type, a_field, cnode);	\
		    rbtn_rotate_left(a_type, a_field, cnode, tnode);	\
		    rbtn_color_set(a_type, a_field, tnode, tred);	\
		    rbtn_red_set(a_type, a_field, cnode);		\
		    cnode = tnode;					\
		}							\
	    } else {							\
		return;							\
	    }								\
	}								\
	pathp->node = cnode;						\
    }									\
    /* Set root, and make it black. */					\
    rbtree->rbt_root = path->node;					\
    rbtn_black_set(a_type, a_field, rbtree->rbt_root);			\
}									\
a_attr void								\
a_prefix##remove(a_rbt_type *rbtree, a_type *node) {			\
    struct {								\
	a_type *node;							\
	int cmp;							\
    } *pathp, *nodep, path[sizeof(void *) << 4];			\
    /* Wind. */								\
    nodep = NULL; /* Silence compiler warning. */			\
    path->node = rbtree->rbt_root;					\
    for (pathp = path; pathp->node != &rbtree->rbt_nil; pathp++) {	\
	int cmp = pathp->cmp = a_cmp(node, pathp->node);		\
	if (cmp < 0) {							\
	    pathp[1].node = rbtn_left_get(a_type, a_field,		\
	      pathp->node);						\
	} else {							\
	    pathp[1].node = rbtn_right_get(a_type, a_field,		\
	      pathp->node);						\
	    if (cmp == 0) {						\
	        /* Find node's successor, in preparation for swap. */	\
		pathp->cmp = 1;						\
		nodep = pathp;						\
		for (pathp++; pathp->node != &rbtree->rbt_nil;		\
		  pathp++) {						\
		    pathp->cmp = -1;					\
		    pathp[1].node = rbtn_left_get(a_type, a_field,	\
		      pathp->node);					\
		}							\
		break;							\
	    }								\
	}								\
    }									\
    assert(nodep->node == node);					\
    pathp--;								\
    if (pathp->node != node) {						\
	/* Swap node with its successor. */				\
	bool tred = rbtn_red_get(a_type, a_field, pathp->node);		\
	rbtn_color_set(a_type, a_field, pathp->node,			\
	  rbtn_red_get(a_type, a_field, node));				\
	rbtn_left_set(a_type, a_field, pathp->node,			\
	  rbtn_left_get(a_type, a_field, node));			\
	/* If node's successor is its right child, the following code */\
	/* will do the wrong thing for the right child pointer.       */\
	/* However, it doesn't matter, because the pointer will be    */\
	/* properly set when the successor is pruned.                 */\
	rbtn_right_set(a_type, a_field, pathp->node,			\
	  rbtn_right_get(a_type, a_field, node));			\
	rbtn_color_set(a_type, a_field, node, tred);			\
	/* The pruned leaf node's child pointers are never accessed   */\
	/* again, so don't bother setting them to nil.                */\
	nodep->node = pathp->node;					\
	pathp->node = node;						\
	if (nodep == path) {						\
	    rbtree->rbt_root = nodep->node;				\
	} else {							\
	    if (nodep[-1].cmp < 0) {					\
		rbtn_left_set(a_type, a_field, nodep[-1].node,		\
		  nodep->node);						\
	    } else {							\
		rbtn_right_set(a_type, a_field, nodep[-1].node,		\
		  nodep->node);						\
	    }								\
	}								\
    } else {								\
	a_type *left = rbtn_left_get(a_type, a_field, node);		\
	if (left != &rbtree->rbt_nil) {					\
	    /* node has no successor, but it has a left child.        */\
	    /* Splice node out, without losing the left child.        */\
	    assert(rbtn_red_get(a_type, a_field, node) == false);	\
	    assert(rbtn_red_get(a_type, a_field, left));		\
	    rbtn_black_set(a_type, a_field, left);			\
	    if (pathp == path) {					\
		rbtree->rbt_root = left;				\
	    } else {							\
		if (pathp[-1].cmp < 0) {				\
		    rbtn_left_set(a_type, a_field, pathp[-1].node,	\
		      left);						\
		} else {						\
		    rbtn_right_set(a_type, a_field, pathp[-1].node,	\
		      left);						\
		}							\
	    }								\
	    return;							\
	} else if (pathp == path) {					\
	    /* The tree only contained one node. */			\
	    rbtree->rbt_root = &rbtree->rbt_nil;			\
	    return;							\
	}								\
    }									\
    if (rbtn_red_get(a_type, a_field, pathp->node)) {			\
	/* Prune red node, which requires no fixup. */			\
	assert(pathp[-1].cmp < 0);					\
	rbtn_left_set(a_type, a_field, pathp[-1].node,			\
	  &rbtree->rbt_nil);						\
	return;								\
    }									\
    /* The node to be pruned is black, so unwind until balance is     */\
    /* restored.                                                      */\
    pathp->node = &rbtree->rbt_nil;					\
    for (pathp--; (uintptr_t)pathp >= (uintptr_t)path; pathp--) {	\
	assert(pathp->cmp != 0);					\
	if (pathp->cmp < 0) {						\
	    rbtn_left_set(a_type, a_field, pathp->node,			\
	      pathp[1].node);						\
	    assert(rbtn_red_get(a_type, a_field, pathp[1].node)		\
	      == false);						\
	    if (rbtn_red_get(a_type, a_field, pathp->node)) {		\
		a_type *right = rbtn_right_get(a_type, a_field,		\
		  pathp->node);						\
		a_type *rightleft = rbtn_left_get(a_type, a_field,	\
		  right);						\
		a_type *tnode;						\
		if (rbtn_red_get(a_type, a_field, rightleft)) {		\
		    /* In the following diagrams, ||, //, and \\      */\
		    /* indicate the path to the removed node.         */\
		    /*                                                */\
		    /*      ||                                        */\
		    /*    pathp(r)                                    */\
		    /*  //        \                                   */\
		    /* (b)        (b)                                 */\
		    /*           /                                    */\
		    /*          (r)                                   */\
		    /*                                                */\
		    rbtn_black_set(a_type, a_field, pathp->node);	\
		    rbtn_rotate_right(a_type, a_field, right, tnode);	\
		    rbtn_right_set(a_type, a_field, pathp->node, tnode);\
		    rbtn_rotate_left(a_type, a_field, pathp->node,	\
		      tnode);						\
		} else {						\
		    /*      ||                                        */\
		    /*    pathp(r)                                    */\
		    /*  //        \                                   */\
		    /* (b)        (b)                                 */\
		    /*           /                                    */\
		    /*          (b)                                   */\
		    /*                                                */\
		    rbtn_rotate_left(a_type, a_field, pathp->node,	\
		      tnode);						\
		}							\
		/* Balance restored, but rotation modified subtree    */\
		/* root.                                              */\
		assert((uintptr_t)pathp > (uintptr_t)path);		\
		if (pathp[-1].cmp < 0) {				\
		    rbtn_left_set(a_type, a_field, pathp[-1].node,	\
		      tnode);						\
		} else {						\
		    rbtn_right_set(a_type, a_field, pathp[-1].node,	\
		      tnode);						\
		}							\
		return;							\
	    } else {							\
		a_type *right = rbtn_right_get(a_type, a_field,		\
		  pathp->node);						\
		a_type *rightleft = rbtn_left_get(a_type, a_field,	\
		  right);						\
		if (rbtn_red_get(a_type, a_field, rightleft)) {		\
		    /*      ||                                        */\
		    /*    pathp(b)                                    */\
		    /*  //        \                                   */\
		    /* (b)        (b)                                 */\
		    /*           /                                    */\
		    /*          (r)                                   */\
		    a_type *tnode;					\
		    rbtn_black_set(a_type, a_field, rightleft);		\
		    rbtn_rotate_right(a_type, a_field, right, tnode);	\
		    rbtn_right_set(a_type, a_field, pathp->node, tnode);\
		    rbtn_rotate_left(a_type, a_field, pathp->node,	\
		      tnode);						\
		    /* Balance restored, but rotation modified        */\
		    /* subree root, which may actually be the tree    */\
		    /* root.                                          */\
		    if (pathp == path) {				\
			/* Set root. */					\
			rbtree->rbt_root = tnode;			\
		    } else {						\
			if (pathp[-1].cmp < 0) {			\
			    rbtn_left_set(a_type, a_field,		\
			      pathp[-1].node, tnode);			\
			} else {					\
			    rbtn_right_set(a_type, a_field,		\
			      pathp[-1].node, tnode);			\
			}						\
		    }							\
		    return;						\
		} else {						\
		    /*      ||                                        */\
		    /*    pathp(b)                                    */\
		    /*  //        \                                   */\
		    /* (b)        (b)                                 */\
		    /*           /                                    */\
		    /*          (b)                                   */\
		    a_type *tnode;					\
		    rbtn_red_set(a_type, a_field, pathp->node);		\
		    rbtn_rotate_left(a_type, a_field, pathp->node,	\
		      tnode);						\
		    pathp->node = tnode;				\
		}							\
	    }								\
	} else {							\
	    a_type *left;						\
	    rbtn_right_set(a_type, a_field, pathp->node,		\
	      pathp[1].node);						\
	    left = rbtn_left_get(a_type, a_field, pathp->node);		\
	    if (rbtn_red_get(a_type, a_field, left)) {			\
		a_type *tnode;						\
		a_type *leftright = rbtn_right_get(a_type, a_field,	\
		  left);						\
		a_type *leftrightleft = rbtn_left_get(a_type, a_field,	\
		  leftright);						\
		if (rbtn_red_get(a_type, a_field, leftrightleft)) {	\
		    /*      ||                                        */\
		    /*    pathp(b)                                    */\
		    /*   /        \\                                  */\
		    /* (r)        (b)                                 */\
		    /*   \                                            */\
		    /*   (b)                                          */\
		    /*   /                                            */\
		    /* (r)                                            */\
		    a_type *unode;					\
		    rbtn_black_set(a_type, a_field, leftrightleft);	\
		    rbtn_rotate_right(a_type, a_field, pathp->node,	\
		      unode);						\
		    rbtn_rotate_right(a_type, a_field, pathp->node,	\
		      tnode);						\
		    rbtn_right_set(a_type, a_field, unode, tnode);	\
		    rbtn_rotate_left(a_type, a_field, unode, tnode);	\
		} else {						\
		    /*      ||                                        */\
		    /*    pathp(b)                                    */\
		    /*   /        \\                                  */\
		    /* (r)        (b)                                 */\
		    /*   \                                            */\
		    /*   (b)                                          */\
		    /*   /                                            */\
		    /* (b)                                            */\
		    assert(leftright != &rbtree->rbt_nil);		\
		    rbtn_red_set(a_type, a_field, leftright);		\
		    rbtn_rotate_right(a_type, a_field, pathp->node,	\
		      tnode);						\
		    rbtn_black_set(a_type, a_field, tnode);		\
		}							\
		/* Balance restored, but rotation modified subtree    */\
		/* root, which may actually be the tree root.         */\
		if (pathp == path) {					\
		    /* Set root. */					\
		    rbtree->rbt_root = tnode;				\
		} else {						\
		    if (pathp[-1].cmp < 0) {				\
			rbtn_left_set(a_type, a_field, pathp[-1].node,	\
			  tnode);					\
		    } else {						\
			rbtn_right_set(a_type, a_field, pathp[-1].node,	\
			  tnode);					\
		    }							\
		}							\
		return;							\
	    } else if (rbtn_red_get(a_type, a_field, pathp->node)) {	\
		a_type *leftleft = rbtn_left_get(a_type, a_field, left);\
		if (rbtn_red_get(a_type, a_field, leftleft)) {		\
		    /*        ||                                      */\
		    /*      pathp(r)                                  */\
		    /*     /        \\                                */\
		    /*   (b)        (b)                               */\
		    /*   /                                            */\
		    /* (r)                                            */\
		    a_type *tnode;					\
		    rbtn_black_set(a_type, a_field, pathp->node);	\
		    rbtn_red_set(a_type, a_field, left);		\
		    rbtn_black_set(a_type, a_field, leftleft);		\
		    rbtn_rotate_right(a_type, a_field, pathp->node,	\
		      tnode);						\
		    /* Balance restored, but rotation modified        */\
		    /* subtree root.                                  */\
		    assert((uintptr_t)pathp > (uintptr_t)path);		\
		    if (pathp[-1].cmp < 0) {				\
			rbtn_left_set(a_type, a_field, pathp[-1].node,	\
			  tnode);					\
		    } else {						\
			rbtn_right_set(a_type, a_field, pathp[-1].node,	\
			  tnode);					\
		    }							\
		    return;						\
		} else {						\
		    /*        ||                                      */\
		    /*      pathp(r)                                  */\
		    /*     /        \\                                */\
		    /*   (b)        (b)                               */\
		    /*   /                                            */\
		    /* (b)                                            */\
		    rbtn_red_set(a_type, a_field, left);		\
		    rbtn_black_set(a_type, a_field, pathp->node);	\
		    /* Balance restored. */				\
		    return;						\
		}							\
	    } else {							\
		a_type *leftleft = rbtn_left_get(a_type, a_field, left);\
		if (rbtn_red_get(a_type, a_field, leftleft)) {		\
		    /*               ||                               */\
		    /*             pathp(b)                           */\
		    /*            /        \\                         */\
		    /*          (b)        (b)                        */\
		    /*          /                                     */\
		    /*        (r)                                     */\
		    a_type *tnode;					\
		    rbtn_black_set(a_type, a_field, leftleft);		\
		    rbtn_rotate_right(a_type, a_field, pathp->node,	\
		      tnode);						\
		    /* Balance restored, but rotation modified        */\
		    /* subtree root, which may actually be the tree   */\
		    /* root.                                          */\
		    if (pathp == path) {				\
			/* Set root. */					\
			rbtree->rbt_root = tnode;			\
		    } else {						\
			if (pathp[-1].cmp < 0) {			\
			    rbtn_left_set(a_type, a_field,		\
			      pathp[-1].node, tnode);			\
			} else {					\
			    rbtn_right_set(a_type, a_field,		\
			      pathp[-1].node, tnode);			\
			}						\
		    }							\
		    return;						\
		} else {						\
		    /*               ||                               */\
		    /*             pathp(b)                           */\
		    /*            /        \\                         */\
		    /*          (b)        (b)                        */\
		    /*          /                                     */\
		    /*        (b)                                     */\
		    rbtn_red_set(a_type, a_field, left);		\
		}							\
	    }								\
	}								\
    }									\
    /* Set root. */							\
    rbtree->rbt_root = path->node;					\
    assert(rbtn_red_get(a_type, a_field, rbtree->rbt_root) == false);	\
}									\
a_attr a_type *								\
a_prefix##iter_recurse(a_rbt_type *rbtree, a_type *node,		\
  a_type *(*cb)(a_rbt_type *, a_type *, void *), void *arg) {		\
    if (node == &rbtree->rbt_nil) {					\
	return (&rbtree->rbt_nil);					\
    } else {								\
	a_type *ret;							\
	if ((ret = a_prefix##iter_recurse(rbtree, rbtn_left_get(a_type,	\
	  a_field, node), cb, arg)) != &rbtree->rbt_nil			\
	  || (ret = cb(rbtree, node, arg)) != NULL) {			\
	    return (ret);						\
	}								\
	return (a_prefix##iter_recurse(rbtree, rbtn_right_get(a_type,	\
	  a_field, node), cb, arg));					\
    }									\
}									\
a_attr a_type *								\
a_prefix##iter_start(a_rbt_type *rbtree, a_type *start, a_type *node,	\
  a_type *(*cb)(a_rbt_type *, a_type *, void *), void *arg) {		\
    int cmp = a_cmp(start, node);					\
    if (cmp < 0) {							\
	a_type *ret;							\
	if ((ret = a_prefix##iter_start(rbtree, start,			\
	  rbtn_left_get(a_type, a_field, node), cb, arg)) !=		\
	  &rbtree->rbt_nil || (ret = cb(rbtree, node, arg)) != NULL) {	\
	    return (ret);						\
	}								\
	return (a_prefix##iter_recurse(rbtree, rbtn_right_get(a_type,	\
	  a_field, node), cb, arg));					\
    } else if (cmp > 0) {						\
	return (a_prefix##iter_start(rbtree, start,			\
	  rbtn_right_get(a_type, a_field, node), cb, arg));		\
    } else {								\
	a_type *ret;							\
	if ((ret = cb(rbtree, node, arg)) != NULL) {			\
	    return (ret);						\
	}								\
	return (a_prefix##iter_recurse(rbtree, rbtn_right_get(a_type,	\
	  a_field, node), cb, arg));					\
    }									\
}									\
a_attr a_type *								\
a_prefix##iter(a_rbt_type *rbtree, a_type *start, a_type *(*cb)(	\
  a_rbt_type *, a_type *, void *), void *arg) {				\
    a_type *ret;							\
    if (start != NULL) {						\
	ret = a_prefix##iter_start(rbtree, start, rbtree->rbt_root,	\
	  cb, arg);							\
    } else {								\
	ret = a_prefix##iter_recurse(rbtree, rbtree->rbt_root, cb, arg);\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = NULL;							\
    }									\
    return (ret);							\
}									\
a_attr a_type *								\
a_prefix##reverse_iter_recurse(a_rbt_type *rbtree, a_type *node,	\
  a_type *(*cb)(a_rbt_type *, a_type *, void *), void *arg) {		\
    if (node == &rbtree->rbt_nil) {					\
	return (&rbtree->rbt_nil);					\
    } else {								\
	a_type *ret;							\
	if ((ret = a_prefix##reverse_iter_recurse(rbtree,		\
	  rbtn_right_get(a_type, a_field, node), cb, arg)) !=		\
	  &rbtree->rbt_nil || (ret = cb(rbtree, node, arg)) != NULL) {	\
	    return (ret);						\
	}								\
	return (a_prefix##reverse_iter_recurse(rbtree,			\
	  rbtn_left_get(a_type, a_field, node), cb, arg));		\
    }									\
}									\
a_attr a_type *								\
a_prefix##reverse_iter_start(a_rbt_type *rbtree, a_type *start,		\
  a_type *node, a_type *(*cb)(a_rbt_type *, a_type *, void *),		\
  void *arg) {								\
    int cmp = a_cmp(start, node);					\
    if (cmp > 0) {							\
	a_type *ret;							\
	if ((ret = a_prefix##reverse_iter_start(rbtree, start,		\
	  rbtn_right_get(a_type, a_field, node), cb, arg)) !=		\
	  &rbtree->rbt_nil || (ret = cb(rbtree, node, arg)) != NULL) {	\
	    return (ret);						\
	}								\
	return (a_prefix##reverse_iter_recurse(rbtree,			\
	  rbtn_left_get(a_type, a_field, node), cb, arg));		\
    } else if (cmp < 0) {						\
	return (a_prefix##reverse_iter_start(rbtree, start,		\
	  rbtn_left_get(a_type, a_field, node), cb, arg));		\
    } else {								\
	a_type *ret;							\
	if ((ret = cb(rbtree, node, arg)) != NULL) {			\
	    return (ret);						\
	}								\
	return (a_prefix##reverse_iter_recurse(rbtree,			\
	  rbtn_left_get(a_type, a_field, node), cb, arg));		\
    }									\
}									\
a_attr a_type *								\
a_prefix##reverse_iter(a_rbt_type *rbtree, a_type *start,		\
  a_type *(*cb)(a_rbt_type *, a_type *, void *), void *arg) {		\
    a_type *ret;							\
    if (start != NULL) {						\
	ret = a_prefix##reverse_iter_start(rbtree, start,		\
	  rbtree->rbt_root, cb, arg);					\
    } else {								\
	ret = a_prefix##reverse_iter_recurse(rbtree, rbtree->rbt_root,	\
	  cb, arg);							\
    }									\
    if (ret == &rbtree->rbt_nil) {					\
	ret = NULL;							\
    }									\
    return (ret);							\
}

#endif /* RB_H_ */




//#include <stdio.h>
//#include <stdlib.h>
//#include <stdbool.h>
//#include <stdint.h>
//#include <strings.h>
//#include <assert.h>
//
//#include "rb.h"
#define	rbtn_black_height(a_type, a_field, a_rbt, r_height) do {	\
    a_type *rbp_bh_t;							\
    for (rbp_bh_t = (a_rbt)->rbt_root, (r_height) = 0;			\
      rbp_bh_t != &(a_rbt)->rbt_nil;					\
      rbp_bh_t = rbtn_left_get(a_type, a_field, rbp_bh_t)) {		\
	if (rbtn_red_get(a_type, a_field, rbp_bh_t) == false) {		\
	    (r_height)++;						\
	}								\
    }									\
} while (0)

#define NNODES 200
#define NSETS 200

static bool verbose = false;
static bool tree_print = false;
static bool forward_print = false;
static bool reverse_print = false;

typedef struct node_s node_t;

struct node_s {
#define NODE_MAGIC 0x9823af7e
    uint32_t magic;
    rb_node(node_t) link;
    long key;
};

static int
nodeCmp(node_t *aA, node_t *aB) {
    int rVal;

    assert(aA->magic == NODE_MAGIC);
    assert(aB->magic == NODE_MAGIC);

    rVal = (aA->key > aB->key) - (aA->key < aB->key);
    if (rVal == 0) {
	// Duplicates are not allowed in the tree, so force an arbitrary
	// ordering for non-identical items with equal keys.
	rVal = (((uintptr_t) aA) > ((uintptr_t) aB))
	  - (((uintptr_t) aA) < ((uintptr_t) aB));
    }
    return rVal;
}

typedef rbt(node_t) tree_t;
rb_gen(static, tree_, tree_t, node_t, link, nodeCmp);

static unsigned
treeRecurse(node_t *aNode, unsigned aBlackHeight, unsigned aBlackDepth,
  node_t *aNil) {
    unsigned rVal = 0;
    node_t *leftNode = rbtn_left_get(node_t, link, aNode);
    node_t *rightNode = rbtn_right_get(node_t, link, aNode);

    if (rbtn_red_get(node_t, link, aNode) == false) {
	aBlackDepth++;
    }

    // Red nodes must be interleaved with black nodes.
    if (rbtn_red_get(node_t, link, aNode)) {
	node_t *tNode = rbtn_left_get(node_t, link, leftNode);
	assert(rbtn_red_get(node_t, link, leftNode) == false);
	tNode = rbtn_right_get(node_t, link, leftNode);
	assert(rbtn_red_get(node_t, link, leftNode) == false);
    }

    if (aNode == aNil) {
	if (tree_print) {
	    fprintf(stderr, ".");
	}
	return rVal;
    }
    /* Self. */
    assert(aNode->magic == NODE_MAGIC);
    if (tree_print) {
	fprintf(stderr, "%ld%c", aNode->key,
	  rbtn_red_get(node_t, link, aNode) ? 'r' : 'b');
    }

    /* Left subtree. */
    if (leftNode != aNil) {
	if (tree_print) {
	    fprintf(stderr, "[");
	}
	rVal += treeRecurse(leftNode, aBlackHeight, aBlackDepth, aNil);
	if (tree_print) {
	    fprintf(stderr, "]");
	}
    } else {
	rVal += (aBlackDepth != aBlackHeight);
	if (tree_print) {
	    fprintf(stderr, ".");
	}
    }

    /* Right subtree. */
    if (rightNode != aNil) {
	if (tree_print) {
	    fprintf(stderr, "<");
	}
	rVal += treeRecurse(rightNode, aBlackHeight, aBlackDepth, aNil);
	if (tree_print) {
	    fprintf(stderr, ">");
	}
    } else {
	rVal += (aBlackDepth != aBlackHeight);
	if (tree_print) {
	    fprintf(stderr, ".");
	}
    }

    return rVal;
}

static node_t *
treeIterateCb(tree_t *aTree, node_t *aNode, void *data) {
    unsigned *i = (unsigned *)data;
    node_t *sNode;

    assert(aNode->magic == NODE_MAGIC);
    if (forward_print) {
	if (*i != 0) {
	    fprintf(stderr, "-->");
	}
	fprintf(stderr, "%ld", aNode->key);
    }

    /* Test rb_search(). */
    sNode = tree_search(aTree, aNode);
    assert(sNode == aNode);

    /* Test rb_nsearch(). */
    sNode = tree_nsearch(aTree, aNode);
    assert(sNode == aNode);

    /* Test rb_psearch(). */
    sNode = tree_psearch(aTree, aNode);
    assert(sNode == aNode);

    (*i)++;

    return NULL;
}

static unsigned
treeIterate(tree_t *aTree) {
    unsigned i;

    i = 0;
    tree_iter(aTree, NULL, treeIterateCb, (void *)&i);

    return i;
}

static node_t *
treeReverseIterateCb(tree_t *aTree, node_t *aNode, void *data) {
    unsigned *i = (unsigned *)data;
    node_t *sNode;

    assert(aNode->magic == NODE_MAGIC);
    if (reverse_print) {
	if (*i != 0) {
	    fprintf(stderr, "<--");
	}
	fprintf(stderr, "%ld", aNode->key);
    }

    /* Test rb_search(). */
    sNode = tree_search(aTree, aNode);
    assert(sNode == aNode);

    /* Test rb_nsearch(). */
    sNode = tree_nsearch(aTree, aNode);
    assert(sNode == aNode);

    /* Test rb_psearch(). */
    sNode = tree_psearch(aTree, aNode);
    assert(sNode == aNode);

    (*i)++;

    return NULL;
}

static unsigned
treeIterateReverse(tree_t *aTree) {
    unsigned i;

    i = 0;
    tree_reverse_iter(aTree, NULL, treeReverseIterateCb, (void *)&i);

    return i;
}

static void
nodeRemove(tree_t *aTree, node_t *aNode, unsigned aNNodes) {
    node_t *sNode;
    unsigned blackHeight, imbalances;

    if (verbose) {
	fprintf(stderr, "rb_remove(%3ld)", aNode->key);
    }
    tree_remove(aTree, aNode);

    /* Test rb_nsearch(). */
    sNode = tree_nsearch(aTree, aNode);
    assert(sNode == NULL || sNode->key >= aNode->key);

    /* Test rb_psearch(). */
    sNode = tree_psearch(aTree, aNode);
    assert(sNode == NULL || sNode->key <= aNode->key);

    aNode->magic = 0;

    if (tree_print) {
	fprintf(stderr, "\n\t   tree: ");
    }
    rbtn_black_height(node_t, link, aTree, blackHeight);
    imbalances = treeRecurse(aTree->rbt_root, blackHeight, 0,
      &(aTree->rbt_nil));
    if (imbalances != 0) {
	fprintf(stderr, "\nTree imbalance\n");
	abort();
    }
    if (forward_print) {
	fprintf(stderr, "\n\tforward: ");
    }
    assert(aNNodes - 1 == treeIterate(aTree));
    if (reverse_print) {
	fprintf(stderr, "\n\treverse: ");
    }
    assert(aNNodes - 1 == treeIterateReverse(aTree));
    if (verbose) {
	fprintf(stderr, "\n");
    }
}

static node_t *
removeIterateCb(tree_t *aTree, node_t *aNode, void *data) {
    unsigned *nNodes = (unsigned *)data;
    node_t *ret = tree_next(aTree, aNode);

    nodeRemove(aTree, aNode, *nNodes);

    return ret;
}

static node_t *
removeReverseIterateCb(tree_t *aTree, node_t *aNode, void *data) {
    unsigned *nNodes = (unsigned *)data;
    node_t *ret = tree_prev(aTree, aNode);

    nodeRemove(aTree, aNode, *nNodes);

    return ret;
}

int
main(void) {
    tree_t tree;
    long set[NNODES];
    node_t nodes[NNODES], key, *sNode, *nodeA;
    unsigned i, j, k, blackHeight, imbalances;

    fprintf(stderr, "Test begin\n");

    /* Initialize tree. */
    tree_new(&tree);

    /*
     * Empty tree.
     */
    fprintf(stderr, "Empty tree:\n");

    /* rb_first(). */
    nodeA = tree_first(&tree);
    if (nodeA == NULL) {
	fprintf(stderr, "rb_first() --> nil\n");
    } else {
	fprintf(stderr, "rb_first() --> %ld\n", nodeA->key);
    }

    /* rb_last(). */
    nodeA = tree_last(&tree);
    if (nodeA == NULL) {
	fprintf(stderr, "rb_last() --> nil\n");
    } else {
	fprintf(stderr, "rb_last() --> %ld\n", nodeA->key);
    }

    /* rb_search(). */
    key.key = 0;
    key.magic = NODE_MAGIC;
    nodeA = tree_search(&tree, &key);
    if (nodeA == NULL) {
	fprintf(stderr, "rb_search(0) --> nil\n");
    } else {
	fprintf(stderr, "rb_search(0) --> %ld\n", nodeA->key);
    }

    /* rb_nsearch(). */
    key.key = 0;
    key.magic = NODE_MAGIC;
    nodeA = tree_nsearch(&tree, &key);
    if (nodeA == NULL) {
	fprintf(stderr, "rb_nsearch(0) --> nil\n");
    } else {
	fprintf(stderr, "rb_nsearch(0) --> %ld\n", nodeA->key);
    }

    /* rb_psearch(). */
    key.key = 0;
    key.magic = NODE_MAGIC;
    nodeA = tree_psearch(&tree, &key);
    if (nodeA == NULL) {
	fprintf(stderr, "rb_psearch(0) --> nil\n");
    } else {
	fprintf(stderr, "rb_psearch(0) --> %ld\n", nodeA->key);
    }

    /* rb_insert(). */
    srandom(42);
    for (i = 0; i < NSETS; i++) {
	if (i == 0) {
	    // Insert in order.
	    for (j = 0; j < NNODES; j++) {
		set[j] = j;
	    }
	} else if (i == 1) {
	    // Insert in reverse order.
	    for (j = 0; j < NNODES; j++) {
		set[j] = NNODES - j - 1;
	    }
	} else {
	    for (j = 0; j < NNODES; j++) {
		set[j] = (long) (((double) NNODES)
		  * ((double) random() / ((double)RAND_MAX)));
	    }
	}

	fprintf(stderr, "Tree %u\n", i);
	for (j = 1; j <= NNODES; j++) {
	    if (verbose) {
		fprintf(stderr, "Tree %u, %u node%s\n",
		  i, j, j != 1 ? "s" : "");
	    }

	    /* Initialize tree and nodes. */
	    tree_new(&tree);
	    tree.rbt_nil.magic = 0;
	    for (k = 0; k < j; k++) {
		nodes[k].magic = NODE_MAGIC;
		nodes[k].key = set[k];
	    }

	    /* Insert nodes. */
	    for (k = 0; k < j; k++) {
		if (verbose) {
		    fprintf(stderr, "rb_insert(%3ld)", nodes[k].key);
		}
		tree_insert(&tree, &nodes[k]);

		if (tree_print) {
		    fprintf(stderr, "\n\t   tree: ");
		}
		rbtn_black_height(node_t, link, &tree, blackHeight);
		imbalances = treeRecurse(tree.rbt_root, blackHeight, 0,
		  &(tree.rbt_nil));
		if (imbalances != 0) {
		    fprintf(stderr, "\nTree imbalance\n");
		    abort();
		}
		if (forward_print) {
		    fprintf(stderr, "\n\tforward: ");
		}
		assert(k + 1 == treeIterate(&tree));
		if (reverse_print) {
		    fprintf(stderr, "\n\treverse: ");
		}
		assert(k + 1 == treeIterateReverse(&tree));
		if (verbose) {
		    fprintf(stderr, "\n");
		}

		sNode = tree_first(&tree);
		assert(sNode != NULL);

		sNode = tree_last(&tree);
		assert(sNode != NULL);

		sNode = tree_next(&tree, &nodes[k]);
		sNode = tree_prev(&tree, &nodes[k]);
	    }

	    /* Remove nodes. */
	    switch (i % 4) {
		case 0: {
		    for (k = 0; k < j; k++) {
			nodeRemove(&tree, &nodes[k], j - k);
		    }
		    break;
		} case 1: {
		    for (k = j; k > 0; k--) {
			nodeRemove(&tree, &nodes[k-1], k);
		    }
		    break;
		} case 2: {
		    node_t *start;
		    unsigned nNodes = j;

		    start = NULL;
		    do {
			start = tree_iter(&tree, start, removeIterateCb,
			  (void *)&nNodes);
			nNodes--;
		    } while (start != NULL);
		    assert(nNodes == 0);
		    break;
		} case 3: {
		    node_t *start;
		    unsigned nNodes = j;

		    start = NULL;
		    do {
			start = tree_reverse_iter(&tree, start,
			  removeReverseIterateCb, (void *)&nNodes);
			nNodes--;
		    } while (start != NULL);
		    assert(nNodes == 0);
		    break;
		} default: {
		    assert(false);
		}
	    }
	}
    }

    fprintf(stderr, "Test end\n");
    return 0;
}
