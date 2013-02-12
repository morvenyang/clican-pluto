//
//  QQIndexDataSource.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "QQIndexRequestModel.h"

@interface QQIndexDataSource : TTListDataSource{
    QQIndexRequestModel* _qqIndexRequestMode;
}

@property (nonatomic,retain) QQIndexRequestModel* qqIndexRequestMode;

- (id)initWithQQChannel:(QQChannel)channel;

@end
