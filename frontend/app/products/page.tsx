"use client";

import { useState, useEffect } from "react";

interface Product {
    id?: number;
    name: string;
    description: string;
    price: number;
}

export default function AdminProductsPage() {
    const [products, setProducts] = useState<Product[]>([]);
    const [form, setForm] = useState<Product>({ name: "", description: "", price: 0 });
    const [editingId, setEditingId] = useState<number | null>(null);

    const API_URL = "http://localhost:8080/products";

    const fetchProducts = async () => {
        try {
            const res = await fetch(API_URL);
            const data = await res.json();
            setProducts(data);
        } catch (error) {
            console.error("Fetch products error:", error);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (editingId) {
                await fetch(`${API_URL}/${editingId}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(form),
                });
                setEditingId(null);
            } else {
                await fetch(API_URL, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(form),
                });
            }
            setForm({ name: "", description: "", price: 0 });
            fetchProducts();
        } catch (error) {
            console.error("Submit error:", error);
        }
    };

    const handleDelete = async (id?: number) => {
        if (!id) return;
        try {
            await fetch(`${API_URL}/${id}`, { method: "DELETE" });
            fetchProducts();
        } catch (error) {
            console.error("Delete error:", error);
        }
    };

    const handleEdit = (product: Product) => {
        setForm(product);
        setEditingId(product.id || null);
    };

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Admin - Products</h1>

            {/* Form */}
            <form onSubmit={handleSubmit} className="mb-6">
                <input
                    type="text"
                    name="name"
                    placeholder="Name"
                    value={form.name}
                    onChange={handleChange}
                    className="border p-2 mr-2"
                    required
                />
                <input
                    type="text"
                    name="description"
                    placeholder="Description"
                    value={form.description}
                    onChange={handleChange}
                    className="border p-2 mr-2"
                    required
                />
                <input
                    type="number"
                    name="price"
                    placeholder="Price"
                    value={form.price}
                    onChange={handleChange}
                    className="border p-2 mr-2"
                    required
                />
                <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
                    {editingId ? "Update" : "Add"}
                </button>
            </form>

            {/* Ürün Listesi */}
            <table className="w-full border-collapse border border-gray-300">
                <thead>
                <tr className="bg-gray-100">
                    <th className="border px-4 py-2">ID</th>
                    <th className="border px-4 py-2">Name</th>
                    <th className="border px-4 py-2">Description</th>
                    <th className="border px-4 py-2">Price</th>
                    <th className="border px-4 py-2">Actions</th>
                </tr>
                </thead>
                <tbody>
                {products.map((p) => (
                    <tr key={p.id}>
                        <td className="border px-4 py-2">{p.id}</td>
                        <td className="border px-4 py-2">{p.name}</td>
                        <td className="border px-4 py-2">{p.description}</td>
                        <td className="border px-4 py-2">{p.price}</td>
                        <td className="border px-4 py-2 space-x-2">
                            <button
                                onClick={() => handleEdit(p)}
                                className="bg-yellow-500 text-white px-2 py-1 rounded"
                            >
                                Edit
                            </button>
                            <button
                                onClick={() => handleDelete(p.id)}
                                className="bg-red-500 text-white px-2 py-1 rounded"
                            >
                                Delete
                            </button>
                        </td>
                    </tr>
                ))}
                {products.length === 0 && (
                    <tr>
                        <td colSpan={5} className="text-center p-4">
                            No products found.
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}
