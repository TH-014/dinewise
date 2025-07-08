import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import MenuManagement from '@/pages/MenuManagement';
import { describe, it, vi, beforeEach, afterEach, expect } from 'vitest';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router-dom';

// Mock useNavigate from react-router-dom
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

// Mock toast from sonner
vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
  },
}));

const mockSuccessResponse = {
  message: 'Menu saved successfully!',
};

const mockFailureResponse = {
  message: 'Failed to save menu!',
};

global.fetch = vi.fn();

const renderComponent = () => {
  return render(
    <BrowserRouter>
      <MenuManagement />
    </BrowserRouter>
  );
};

describe('MenuManagement Page', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.resetAllMocks();
  });

  it('renders all form fields and button', () => {
    renderComponent();

    expect(screen.getByText('Create/Update Menu')).toBeInTheDocument();
    expect(screen.getByLabelText('Date (YYYY-MM-DD)')).toBeInTheDocument();
    expect(screen.getByLabelText('Lunch Items (comma-separated)')).toBeInTheDocument();
    expect(screen.getByLabelText('Dinner Items (comma-separated)')).toBeInTheDocument();
    expect(screen.getByText('Save Menu')).toBeInTheDocument();
  });

  it('submits the menu successfully and navigates', async () => {
    (fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockSuccessResponse,
    });

    renderComponent();

    fireEvent.change(screen.getByLabelText('Date (YYYY-MM-DD)'), {
      target: { value: '2025-07-10' },
    });
    fireEvent.change(screen.getByLabelText('Lunch Items (comma-separated)'), {
      target: { value: 'Rice, Chicken Curry' },
    });
    fireEvent.change(screen.getByLabelText('Dinner Items (comma-separated)'), {
      target: { value: 'Paratha, Egg Curry' },
    });

    fireEvent.click(screen.getByText('Save Menu'));

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        `${import.meta.env.VITE_API_BASE_URL}/menus`,
        expect.objectContaining({
          method: 'POST',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            menuDate: '2025-07-10',
            lunchItems: ['Rice', 'Chicken Curry'],
            dinnerItems: ['Paratha', 'Egg Curry'],
          }),
        })
      );
    });

    const { toast } = await import('sonner');
    expect(toast.success).toHaveBeenCalledWith('Menu saved successfully!');
    expect(mockNavigate).toHaveBeenCalledWith('/manager/dashboard');
  });

  it('shows error toast when API call fails (400+)', async () => {
    (fetch as any).mockResolvedValueOnce({
      ok: false,
      json: async () => mockFailureResponse,
    });

    renderComponent();

    fireEvent.change(screen.getByLabelText('Date (YYYY-MM-DD)'), {
      target: { value: '2025-07-10' },
    });
    fireEvent.change(screen.getByLabelText('Lunch Items (comma-separated)'), {
      target: { value: 'Rice, Lentils' },
    });
    fireEvent.change(screen.getByLabelText('Dinner Items (comma-separated)'), {
      target: { value: 'Roti, Paneer' },
    });

    fireEvent.click(screen.getByText('Save Menu'));

    await waitFor(async () => {
      const { toast } = await import('sonner');
      expect(toast.error).toHaveBeenCalledWith('Failed to save menu!');
    });
  });

  it('shows error toast on network failure', async () => {
    (fetch as any).mockRejectedValueOnce(new Error('Network error'));

    renderComponent();

    fireEvent.change(screen.getByLabelText('Date (YYYY-MM-DD)'), {
      target: { value: '2025-07-10' },
    });
    fireEvent.change(screen.getByLabelText('Lunch Items (comma-separated)'), {
      target: { value: 'Veg Biryani' },
    });
    fireEvent.change(screen.getByLabelText('Dinner Items (comma-separated)'), {
      target: { value: 'Dal, Roti' },
    });

    fireEvent.click(screen.getByText('Save Menu'));

    await waitFor(async () => {
      const { toast } = await import('sonner');
      expect(toast.error).toHaveBeenCalledWith('Network error. Please try again.');
    });
  });
});
