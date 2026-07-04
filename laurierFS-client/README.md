# Laurier Food Services - Client

Front-end for the Laurier Food Services supply chain and logistics management system.

## Tech Stack
- React.js (Vite)
- React Router DOM

## Prerequisites
- Node.js (LTS) - download from nodejs.org
- Git

## Setup
1. Clone the repository
2. Navigate into the client folder:
   cd laurierFS-client
3. Install dependencies:
   npm install
4. Start the development server:
   npm run dev
5. Open http://localhost:5173 in your browser

## Branching Rules
- Never work directly on main
- Always create a feature branch: feature/page-name
- Open a Pull Request into main when your page is complete
- Pull latest main into your branch daily

## Branch Naming Convention
- feature/home-page
- feature/product-details
- feature/shopping-cart
- feature/checkout
- feature/admin-dashboard

## Project Structure
src/
  components/   reusable building blocks
  pages/        full page components
  services/     data fetching layer
  data/         mock data
  styles/       CSS files