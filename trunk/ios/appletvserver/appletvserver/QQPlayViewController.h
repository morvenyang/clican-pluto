//
//  QQPlayViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQPlayRequestModel.h"
#import "MediaPlayer/MediaPlayer.h"

@interface QQPlayViewController : UIViewController<QQPlayDelegate>{
    QQPlayRequestModel* _qqPlayRequestModel;
    MPMoviePlayerViewController* _playerViewController;
    NSString* _vid;
}
@property (nonatomic, retain) QQPlayRequestModel* qqPlayRequestModel;
@property (nonatomic, retain) MPMoviePlayerViewController* playerViewController
;
@property (nonatomic, copy) NSString* vid;

- (id)initWithVideoItemId:(NSString*)videoItemId vid:(NSString*)vid;
@end
