"use client";

import { useEffect, useState } from "react";

interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
}

export default function ProductsPage() {
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch("http://localhost:8080/products")
            .then((res) => res.json())
            .then((data) => setProducts(data))
            .catch((err) => console.error(err))
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p className="p-6">Yükleniyor...</p>;

    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold mb-6">Ürünler</h1>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                {products.map((product) => (
                    <div
                        key={product.id}
                        className="border p-4 rounded shadow hover:shadow-lg transition"
                    >
                        <h2 className="text-xl font-semibold">{product.name}</h2>
                        <p className="text-gray-600">{product.description}</p>
                        <p className="mt-2 font-bold">${product.price}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}
