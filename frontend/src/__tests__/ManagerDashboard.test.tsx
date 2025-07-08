import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import ManagerDashboard from '@/pages/ManagerDashboard';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import { toast } from 'sonner';

// Mock useNavigate
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

// Mock toast

const toastMock = { error: vi.fn() };
// vi.mock('sonner', () => ({ toast: toastMock }));

// ✅ Inline mock definition
vi.mock('sonner', () => ({
  toast: {
    error: vi.fn(),
    success: vi.fn(),
  },
}));


// Mock fetch globally
global.fetch = vi.fn();

const renderComponent = () => render(<BrowserRouter><ManagerDashboard /></BrowserRouter>);

describe('ManagerDashboard Page', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders loading state initially', async () => {
    (fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ lunchCount: 10, dinnerCount: 20 }),
    });

    renderComponent();

    expect(screen.getByText(/Lunch Confirmations \(Tomorrow\)/)).toBeInTheDocument();
    expect(screen.getByText(/Dinner Confirmations \(Tomorrow\)/)).toBeInTheDocument();

    expect(screen.getAllByText('Loading...').length).toBe(2);

    await waitFor(() => {
      expect(screen.getByText('10')).toBeInTheDocument();
      expect(screen.getByText('20')).toBeInTheDocument();
    });
  });

  it('renders correct stats from API response', async () => {
    (fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ lunchCount: 5, dinnerCount: 7 }),
    });

    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('5')).toBeInTheDocument();
      expect(screen.getByText('7')).toBeInTheDocument();
    });
  });

//   it('shows error toast if API fails', async () => {
//     (fetch as any).mockResolvedValueOnce({ ok: false });

//     renderComponent();

//     await waitFor(() => {
//       expect(toastMock.error).toHaveBeenCalledWith('Failed to fetch dashboard stats');
//     });
//   });
    it('shows error toast if API fails', async () => {
    // ❗ Force fetch to throw
    (fetch as any).mockImplementationOnce(() => {
        return Promise.reject(new Error('Failed to fetch dashboard stats'));
    });

    renderComponent();

    await waitFor(() => {
        expect(toast.error).toHaveBeenCalledWith('Failed to fetch dashboard stats');
    });
    });


  it('navigates to stock page on button click', async () => {
    (fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ lunchCount: 3, dinnerCount: 6 }),
    });

    renderComponent();

    const stockButton = await screen.findByText('View Stock Storage');
    fireEvent.click(stockButton);

    expect(mockNavigate).toHaveBeenCalledWith('/stocks');
  });

  it('navigates to menu management page on button click', async () => {
    (fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ lunchCount: 2, dinnerCount: 4 }),
    });

    renderComponent();

    const menuButton = await screen.findByText('Set or Update Menu');
    fireEvent.click(menuButton);

    expect(mockNavigate).toHaveBeenCalledWith('/manager/menu');
  });
});
