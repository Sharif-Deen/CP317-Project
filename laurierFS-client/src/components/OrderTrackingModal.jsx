import React from 'react';
import '../styles/OrderTrackingModal.css';

const OrderTrackingModal = ({ isOpen, onClose, orderData }) => {
    if (!isOpen) return null;

    const steps = ['Ordered', 'Confirmed', 'Shipped', 'Delivered'];
    // Logic: If order status is 'confirmed', set step to 1, etc.
    // For now, mapping status string to index:
    const statusMap = { 'Ordered': 0, 'Confirmed': 1, 'Shipped': 2, 'Delivered': 3 };
    const currentStepIndex = statusMap[orderData.status] ?? 1; // Default to 1 (Confirmed)

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <h2 style={{ marginBottom: '20px' }}>Order Tracking: ORD-{orderData.id}</h2>
                
                <div className="stepper">
                    {steps.map((step, index) => (
                        <div key={step} className={`step ${index <= currentStepIndex ? 'active' : ''}`}>
                            <div className="step-circle">
                                {index < currentStepIndex ? '✓' : index + 1}
                            </div>
                            <div className="step-label">{step}</div>
                        </div>
                    ))}
                </div>

                <div className="shipping-info">
                    <p><strong>Courier:</strong> Canada Post</p>
                    <p><strong>Tracking #:</strong> CP123456789CA</p>
                </div>
                
                <button className="close-btn" onClick={onClose}>Close</button>
            </div>
        </div>
    );
};

export default OrderTrackingModal;