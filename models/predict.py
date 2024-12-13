import tensorflow as tf
import numpy as np
import cv2

model = tf.keras.models.load_model('train/sola_model.h5')
class_names = ['Bird_Drop', 'Physical_Damage', 'Clean', 'Snow_Covered', 'Dusty', 'Electrical_Damage']

def predict_insect(image_path):
    image_size = (128, 128)
    img = cv2.imread(image_path)
    if img is not None:
        img = cv2.resize(img, image_size)
        img = img / 255.0 
        img = np.expand_dims(img, axis=0)  
        predictions = model.predict(img)
        predicted_class = np.argmax(predictions, axis=1)[0]
        confidence = predictions[0][predicted_class]
        return class_names[predicted_class], confidence
    else:
        raise ValueError(f"Image not found at {image_path}")

image_path = 'test/test2.jpg'
predicted_class, confidence = predict_insect(image_path)
print(f"Predicted class: {predicted_class} with confidence: {confidence:.2f}")
