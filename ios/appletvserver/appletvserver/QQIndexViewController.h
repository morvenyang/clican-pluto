//
//  QQIndexViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "Constants.h"

@interface QQIndexViewController : TTTableViewController{
    QQChannel _channelId;
}

@property (nonatomic, assign) QQChannel channelId;

- (id) initWithChannelId:(QQChannel) channelId;
@end

