//
//  FFMpegPlayViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-15.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VideoFrameExtractor.h"

@interface FFMpegPlayViewController : UIViewController{
    UIImageView* _imageView;
	VideoFrameExtractor* _video;
    float lastFrameTime;
}

@property (nonatomic, retain) UIImageView *imageView;
@property (nonatomic, retain) VideoFrameExtractor *video;

@end
