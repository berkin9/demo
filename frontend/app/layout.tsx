import './globals.css'

export const metadata = {
    title: 'E-Commerce Platform',
    description: 'Modern e-commerce platform',
}

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode
}) {
    return (
        <html lang="en">
        <body className="bg-gray-100">{children}</body>
        </html>
    )
}
