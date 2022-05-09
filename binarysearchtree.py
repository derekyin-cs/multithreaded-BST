class BinarySearchTree:


    def __init__(self, name, root):
        self.data = name
        self.root = root
        self.sorted = []

    def add_all(self, *args):
        for element in args:
            if len(self.sorted) == 0:
                self.root = Node(element)
                self.sorted.append(element)
            else:
                self.sorted.append(element)
                self.insert_BST(self.root, element)


    def insert_BST(self, root, element):
        if element > root.name:
            if root.right is None:
                root.right = Node(element)
            else:
                self.insert_BST(root.right, element)

        else:
            if root.left is None:
                root.left = Node(element)
            else:
                self.insert_BST(root.left, element)


    def __str__(self):
        return str("[" + str(self.data) + "] " + self.printhelper(self.root))

    def printhelper(self, root):
        if root is None:
            return ""

        string = str(root.name)
        if (root.left is not None):
            string += str(" L:(" + self.printhelper(root.left) + ")")
        if (root.right is not None):
            string += str(" R:(" + self.printhelper(root.right) + ")")

        return string

class Node:
    def __init__(self, name = None):
        self.name = name
        self.left = None
        self.right = None

    def __iter__(self):
        self.i = 0
        self.vals = self.inorder()

        return self

    def __next__(self):
        if self.i < len(self.vals):
            val = self.vals[self.i]
            self.i+=1
            return val
        else:
            raise StopIteration

    def inorder(self):
        lst = []
        if self is None:
            return lst

        if (self.left != None):
            lst.extend(self.left.inorder())

        lst.append(self.name)

        if (self.right != None):
            lst.extend(self.right.inorder())

        return lst


if __name__ == "__main__":
    t1 = BinarySearchTree(name="Oak", root=Node())
    t2 = BinarySearchTree(name="Birch", root=Node())
    t1.add_all(5, 3, 9, 0)
    t2.add_all(1, 0, 10, 2, 7)
    for x in t1.root:
        print(x)
    for x in t2.root:
        print(x)

    tree = BinarySearchTree(name="Oak", root=Node())
    tree.add_all(5, 3, 9, 0)  # adds the elements in the order 5, 3, 9, and then 0
    print(tree)
