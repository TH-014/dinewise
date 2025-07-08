import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import ManagerStockView from '@/pages/ManagerStockView';
import React from 'react';
import { describe, it, beforeEach, vi, Mock, expect } from 'vitest';
import '@testing-library/jest-dom';

// ⛳️ Mock fetch globally
global.fetch = vi.fn();

// 🔔 Mock toast notifications
vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
  },
}));

describe('ManagerStockView', () => {
  beforeEach(() => {
    (fetch as Mock).mockClear();
  });

  it('fetches and displays stock items', async () => {
    const mockStocks = [
      {
        id: 1,
        itemName: 'Rice',
        unit: 'kg',
        quantity: 10,
        perUnitPrice: 50,
        lastUpdated: new Date().toISOString(),
      },
    ];

    (fetch as Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => mockStocks,
    });

    render(<ManagerStockView />);

    expect(await screen.findByText('Stock Inventory')).toBeInTheDocument();
    expect(await screen.findByText('Rice')).toBeInTheDocument();
    expect(fetch).toHaveBeenCalledWith('http://localhost:8080/stocks', { credentials: 'include' });
  });

  it('opens the dialog and submits new item', async () => {
    (fetch as Mock)
      .mockResolvedValueOnce({ ok: true, json: async () => [] }) // initial fetch
      .mockResolvedValueOnce({ ok: true }) // POST/PUT
      .mockResolvedValueOnce({ ok: true, json: async () => [] }); // re-fetch

    render(<ManagerStockView />);

    fireEvent.click(screen.getByText('Add Item'));

    fireEvent.change(screen.getByPlaceholderText('Item Name'), { target: { value: 'Oil' } });
    fireEvent.change(screen.getByPlaceholderText('Unit (e.g. kg, litre)'), { target: { value: 'litre' } });
    fireEvent.change(screen.getByPlaceholderText('Quantity'), { target: { value: '5' } });
    fireEvent.change(screen.getByPlaceholderText('Price per Unit'), { target: { value: '100' } });

    fireEvent.click(screen.getByText('Add'));

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        'http://localhost:8080/stocks',
        expect.objectContaining({
          method: 'POST',
          body: JSON.stringify({
            itemName: 'Oil',
            unit: 'litre',
            quantity: '5',
            perUnitPrice: '100',
          }),
        })
      );
    });
  });

//   it('opens edit mode and updates item', async () => {
//     const mockStock = {
//       id: 2,
//       itemName: 'Sugar',
//       unit: 'kg',
//       quantity: 20,
//       perUnitPrice: 60,
//       lastUpdated: new Date().toISOString(),
//     };

//     (fetch as Mock)
//       .mockResolvedValueOnce({ ok: true, json: async () => [mockStock] }) // initial fetch
//       .mockResolvedValueOnce({ ok: true }) // PUT call
//       .mockResolvedValueOnce({ ok: true, json: async () => [] }); // re-fetch

//     render(<ManagerStockView />);

//     await screen.findByText('Sugar');
//     fireEvent.click(screen.getAllByText('Edit')[0]);

//     const quantityInput = screen.getByPlaceholderText('Quantity');
//     fireEvent.change(quantityInput, { target: { value: '25' } });

//     fireEvent.click(screen.getByText('Update'));

//     await waitFor(() => {
//       expect(fetch).toHaveBeenCalledWith(
//         'http://localhost:8080/stocks/2',
//         expect.objectContaining({
//           method: 'PUT',
//           body: JSON.stringify(expect.objectContaining({ quantity: '25' })),
//         })
//       );
//     });
//   });

it('opens edit mode and updates item', async () => {
  const mockStock = {
    id: 2,
    itemName: 'Sugar',
    unit: 'kg',
    quantity: 20,
    perUnitPrice: 60,
    lastUpdated: new Date().toISOString(),
  };

  (fetch as Mock)
    .mockResolvedValueOnce({ ok: true, json: async () => [mockStock] }) // initial fetch
    .mockResolvedValueOnce({ ok: true }) // PUT call
    .mockResolvedValueOnce({ ok: true, json: async () => [] }); // re-fetch after update

  render(<ManagerStockView />);

  // Wait for Sugar row
  await screen.findByText('Sugar');

  // Click Edit
  fireEvent.click(screen.getByText('Edit'));

  // Wait for input to be present
  await waitFor(() => {
    expect(screen.getByDisplayValue('20')).toBeInTheDocument();
  });

  // Change Quantity
  fireEvent.change(screen.getByPlaceholderText('Quantity'), { target: { value: '25' } });

  // Click Update
  fireEvent.click(screen.getByText('Update'));

  await waitFor(() => {
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8080/stocks/2',
      expect.objectContaining({
        method: 'PUT',
        body: JSON.stringify({
          itemName: 'Sugar',
          unit: 'kg',
          quantity: '25',
          perUnitPrice: '60',
        }),
      })
    );
  });
});



});
