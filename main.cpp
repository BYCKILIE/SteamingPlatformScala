#include <iostream>
#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;

int main(int argc, char** argv) {
    if (argc != 3) {
        cout << "Usage: ./video_breaker <input_video_file> <destination>" << endl;
        return -1;
    }

    string inputFilename = argv[1];
    string destination = argv[2];

    string mkdirVideos = "mkdir " + destination + "\\videos";
    string mkdirAudios = "mkdir " + destination + "\\audios";
    system(mkdirVideos.c_str());
    system(mkdirAudios.c_str());

    VideoCapture video(inputFilename);
    if (!video.isOpened()) {
        cout << "Error: Unable to open the video file." << endl;
        return -1;
    }

    double fps = video.get(CAP_PROP_FPS);
    int totalFrames = static_cast<int>(video.get(CAP_PROP_FRAME_COUNT));
    int desiredFramesPerClip = static_cast<int>(fps * 60); // 1 minute

    int clipCount = 0;
    int frameCount = 0;
    VideoWriter outputVideo;
    Mat frame;

    while (true) {
        if (!video.read(frame))
            break;

        if (!outputVideo.isOpened()) {
            string outputVideoFilename = destination + "\\videos\\chunk_" + to_string(clipCount) + ".avi";
            outputVideo.open(outputVideoFilename, VideoWriter::fourcc('M','J','P','G'), fps, Size(frame.cols, frame.rows));
        }

        outputVideo.write(frame);
        frameCount++;

        if (frameCount >= desiredFramesPerClip) {
            outputVideo.release();
            clipCount++;
            frameCount = 0;
        }

        if (clipCount > totalFrames / desiredFramesPerClip)
            break;
    }
    int remainingFrames = totalFrames - (clipCount - 1) * desiredFramesPerClip;
    if (remainingFrames > 0) {
        string outputVideoFilename = destination + "\\videos\\chunk_" + to_string(clipCount) + ".avi";
        VideoWriter remainingVideo(outputVideoFilename, VideoWriter::fourcc('M','J','P','G'), fps, Size(frame.cols, frame.rows));
        while (true) {
            if (!video.read(frame))
                break;
            remainingVideo.write(frame);
        }
        remainingVideo.release();
    }

    video.release();

    // Splitting audio
    string command = "ffmpeg -i " + inputFilename + " -vn -acodec pcm_s16le -ar 44100 -ac 2 -f segment -segment_time 60 " + destination + "\\audios\\chunk_%d.wav";
    system(command.c_str());

    return 0;
}
