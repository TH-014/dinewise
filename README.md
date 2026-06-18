# DineWise 🍽️

A full-stack web application for managing university dormitory meal systems — built to replace manual, error-prone mess management with a modern, role-based digital platform.

---

## Overview

DineWise digitizes the complete lifecycle of a university dorm mess: from daily menu planning and student meal confirmations to inventory tracking, payment processing, and AI-assisted menu suggestions. It serves three types of users — **Students**, **Mess Managers**, and **Dorm Admins** — each with a tailored set of features and access controls.

---

## Features

### For Students

- **Registration & Authentication** — Sign up with student ID, email verification via OTP, and secure login.
- **Daily Menu View** — View the up-to-date lunch and dinner menu for any day, published by the mess manager.
- **Meal Confirmation** — Submit daily preferences (lunch / dinner / both / neither) so the manager can plan quantities accurately.
- **Comments & Ratings** — Post comments on daily menus, reply to others in threaded discussions, and give star ratings (1–5) for meals.
- **Due Tracking** — View accumulated dues calculated from meals actually eaten (not flat-rate).
- **Online Payment** — Pay dues directly through an integrated payment gateway supporting bKash, Rocket, and card payments.
- **Manager Application** — Apply to become the next mess manager; dorm admins review applications and decide based on interest and track record.
- **Notifications** — Receive in-app notifications from the manager or admin (read/unread tracking).

### For Mess Managers

- **Menu Publishing** — Create and publish daily lunch and dinner menus.
- **AI-Assisted Menu Suggestions** — Get intelligent menu recommendations from an LLM-powered assistant, taking into account current stock levels, the number of students eating, market availability, budget constraints, and recent menu history to avoid repetition.
- **Stock & Inventory Management** — Track raw ingredient stock (quantity, unit, price per unit), log market purchases, and record daily consumption.
- **Expense Tracking** — Log purchases with itemized breakdowns; view historical expenditure by date.
- **Student Confirmation Dashboard** — See how many students confirmed lunch and dinner for any given date to optimize cooking quantities.
- **Feedback Analysis** — View AI-analyzed summaries of student ratings and comments to improve meal quality.
- **Notifications** — Send announcements to students or all users.

### For Dorm Admins

- **Manager Application Review** — Approve or reject student applications to become the next mess manager.
- **Manager Oversight** — Maintain a history of past and active managers with performance data (average rating, tenure).
- **System-wide Notifications** — Broadcast messages to students or managers.

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java (Spring Boot) |
| **Frontend** | TypeScript (React) |
| **Database** | PostgreSQL 17 |
| **AI Integration** | LLM-powered menu suggestion and feedback analysis |
| **Payment** | bKash, Rocket, Card (integrated gateway) |
| **Containerization** | Docker & Docker Compose |
| **CI/CD** | GitHub Actions |

---

## Architecture

The project follows a standard **three-tier architecture**:

```
┌─────────────────────┐
│   React Frontend    │  (TypeScript, port 8082)
└────────┬────────────┘
         │ REST API
┌────────▼────────────┐
│  Spring Boot Backend│  (Java, port 8080)
└────────┬────────────┘
         │
┌────────▼────────────┐
│   PostgreSQL DB     │  (port 5432)
└─────────────────────┘
```

All three services are orchestrated via Docker Compose for one-command local setup.

---

## Database Schema (Key Entities)

The PostgreSQL schema includes the following core tables:

- `student`, `admin` — User accounts with role-based access (`student`, `manager`, `admin`)
- `user_request` — Pending registrations awaiting OTP email verification
- `mess_manager` — Active and past manager records with average ratings
- `manager_applications` — Student applications for the manager role (pending / approved / rejected)
- `meal_confirmations` — Per-student, per-date lunch/dinner preferences
- `menus` — Daily lunch and dinner item lists
- `stocks` — Ingredient inventory with quantity, unit, and pricing
- `expenses` / `expense_items` — Itemized purchase records
- `market_expenses` / `daily_expenses` — Separate tracking for stock additions vs. consumption
- `dues` / `payments` — Student financial records and payment history
- `feedback` — Meal ratings and comments (supports anonymous submissions)
- `comment_thread` / `comment` — Threaded discussion system per menu date
- `sent_notifications` / `received_notifications` — Notification delivery and read-status tracking
- `ai_menu_suggestions` — Stored AI-generated menu suggestion payloads
- `chatbot_conversations` — Manager AI chat session history
- `feedback_analysis` — AI-analyzed summaries of student feedback

---

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose installed

### Run with Docker Compose

```bash
git clone https://github.com/TH-014/dinewise.git
cd dinewise
docker compose up --build
```

This starts all three services:

| Service | URL |
|---|---|
| Frontend | http://localhost:8082 |
| Backend API | http://localhost:8080 |
| PostgreSQL | localhost:5432 |

### Database Setup

To initialize the database schema manually (if not using Docker):

```bash
psql -U postgres -d dinewiseDB -f Dinewise.sql
```

Default admin credentials (seeded by `Dinewise.sql`):

| Username | Email | Password |
|---|---|---|
| `admin1` | admin1@example.com | `admin` (bcrypt hashed) |
| `admin2` | admin2@example.com | `admin` |
| `admin3` | admin3@example.com | `admin` |

> **Note:** Change these credentials before any production deployment.

---

## Project Structure

```
dinewise/
├── dinewise/          # Spring Boot backend (Java)
├── frontend/          # React frontend (TypeScript)
├── Dinewise.sql       # PostgreSQL schema & seed data
├── docker-compose.yml # Multi-service orchestration
└── .github/workflows/ # CI/CD pipelines
```

---

## User Roles

| Role | Key Capabilities |
|---|---|
| **Student** | View menu, confirm meals, pay dues, comment & rate, apply for manager |
| **Manager** | Publish menus, manage stock & expenses, use AI suggestions, view confirmations |
| **Admin** | Approve manager applications, manage users, broadcast notifications |

---

## AI-Powered Features

DineWise integrates an LLM-based assistant for two key functions:

**Menu Suggestion** — The manager interacts with a conversational AI that considers:
- Current stock availability
- Number of students confirmed for the next meal
- Market availability of ingredients
- Budget for the meal period
- Previous menu history (to avoid repetition)

**Feedback Analysis** — Aggregated student ratings and comments are periodically analyzed by the AI to surface actionable insights for the manager.

---

## Payment Integration (via SSLCommerz)

Students can settle meal dues through the in-app payment system, which supports:
- **bKash** (mobile banking)
- **Rocket** (mobile banking)
- **Card** (debit/credit)

Each payment records a unique transaction ID and timestamp for audit purposes.

---

## License

This project is currently unlicensed. All rights reserved by the authors.
