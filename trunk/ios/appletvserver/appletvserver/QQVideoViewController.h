//
//  QQVideoViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "Video.h"
#import "QQVideoRequestModel.h"

@interface QQVideoViewController : TTViewController<QQVideoDelegate>{
    Video* _video;
    QQVideoRequestModel* _qqVideoRequestModel;
}

@property (nonatomic, retain) Video* video;
@property (nonatomic, retain) QQVideoRequestModel* qqVideoRequestModel;

- (id) initWithVid:(NSNumber*) vid;

@end
