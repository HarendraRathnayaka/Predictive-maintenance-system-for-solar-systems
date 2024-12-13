import tensorflow as tf
from tensorflow.keras import layers, models
from sklearn.model_selection import train_test_split
import numpy as np
import cv2
import os


image_data = []
labels = []
image_size = (128, 128)
class_names = ['Bird_Drop', 'Physical_Damage', 'Clean', 'Snow_Covered', 'Dusty', 'Electrical_Damage']

data_dir = 'images/'

for label in class_names:
    path = os.path.join(data_dir, label)
    for img_name in os.listdir(path):
        img_path = os.path.join(path, img_name)
        img = cv2.imread(img_path)
        if img is not None:
            img = cv2.resize(img, image_size)
            image_data.append(img)
            labels.append(class_names.index(label))

image_data = np.array(image_data) / 255.0
labels = np.array(labels)

X_train, X_test, y_train, y_test = train_test_split(image_data, labels, test_size=0.3, random_state=42)


model = models.Sequential([
    layers.Conv2D(32, (3, 3), activation='relu', input_shape=(128, 128, 3)),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(64, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(128, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Flatten(),
    layers.Dense(128, activation='relu'),
    layers.Dense(len(class_names), activation='softmax')  # 3 classes, use softmax for multi-class
])

model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

history = model.fit(X_train, y_train, epochs=100, batch_size=32, validation_data=(X_test, y_test))

test_loss, test_acc = model.evaluate(X_test, y_test)
print(f'Test accuracy: {test_acc}')

model.save('sola_model.h5')
