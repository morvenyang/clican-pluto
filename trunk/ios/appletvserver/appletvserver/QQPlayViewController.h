//
//  QQPlayViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQPlayRequestModel.h"
#import "MediaPlayer/MediaPlayer.h"

@interface QQPlayViewController : TTViewController<QQPlayDelegate>{
    QQPlayRequestModel* _qqPlayRequestModel;
    MPMoviePlayerController* _moviePlayer;
}
@property (nonatomic, retain) QQPlayRequestModel* qqPlayRequestModel;
@property (nonatomic, retain) MPMoviePlayerController* moviePlayer
;
- (id)initWithVideoItemId:(NSString*)videoItemId;
@end
