
import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Index from "./pages/Index";
// import Signup from "./pages/SignupWithOTP";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import NotFound from "./pages/NotFound";
import SignupWithOTP from "./pages/SignupWithOTP";
import ManagerLogin from './pages/ManagerLogin';
import ManagerDashboard from "./pages/ManagerDashboard";
import ManagerStockView from "./pages/ManagerStockView";
import MenuManagement from "./pages/MenuManagement";
import MealsSinceLastPayment from '@/pages/MealsSinceLastPayment';

import CommentsThreadPage from './pages/CommentsThreadPage';
import ExpenseAddPage from "./pages/ExpenseAddPage";
import ManagerStats from "./pages/ManagerStats";





const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Index />} />
          <Route path="/signup/request" element={<SignupWithOTP />} />
          <Route path="/login" element={<Login />} />
          <Route path="/manager-login" element={<ManagerLogin />} />
          <Route path="/manager/dashboard" element={<ManagerDashboard />} />
          <Route path="/stocks" element={<ManagerStockView />} />
          <Route path="/manager/menu" element={<MenuManagement />} />
          <Route path="/meals-since-last-payment" element={<MealsSinceLastPayment />} />

          <Route path="/manager/expense" element={<ExpenseAddPage />} />

          <Route path="/comments" element={<CommentsThreadPage />} />

          <Route path="/manager/stats" element={<ManagerStats />} />

          <Route 
            path="/dashboard" 
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } 
          />
          {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
