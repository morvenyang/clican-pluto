//
//  FFMpegPlayViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-15.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VideoFrameExtractor.h"
#import "Player.h"
@interface FFMpegPlayViewController : UIViewController{
    UIImageView* _imageView;
	VideoFrameExtractor* _video;
    float lastFrameTime;
    Player* _player;
    
}

@property (nonatomic, retain) UIImageView *imageView;
@property (nonatomic, retain) VideoFrameExtractor *video;
@property (nonatomic, retain) Player *player;
@end
