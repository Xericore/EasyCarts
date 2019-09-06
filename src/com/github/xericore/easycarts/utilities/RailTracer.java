package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.data.RailTrace;
import com.github.xericore.easycarts.data.TracedRail;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class RailTracer
{
    private List<TracedRail> _tracedRailShapes = new ArrayList<>();
    private int _maxTraceLength;

    public List<TracedRail> traceRails(Block initialBlock, BlockFace initialFacing, int maxTraceLength)
    {
        _maxTraceLength = maxTraceLength;
        _tracedRailShapes.clear();

        if(!RailUtils.isRail(initialBlock))
            return _tracedRailShapes;

        _tracedRailShapes.add(new TracedRail(initialBlock));

        traceNextRailRecursive(initialBlock, initialFacing);

        return _tracedRailShapes;
    }

    private void traceNextRailRecursive(Block block, BlockFace facing)
    {
        _maxTraceLength--;

        while (_maxTraceLength > 0)
        {
            if(facing == null || block == null)
            {
                _maxTraceLength = 0;
                return;
            }

            RailTrace nextTrace = new RailTrace(block, facing);

            if(nextTrace.getNextTracedRail() == null)
            {
                _maxTraceLength = 0;
                return;
            }

            _tracedRailShapes.add(nextTrace.getNextTracedRail());

            traceNextRailRecursive(nextTrace.getNextBlock(), nextTrace.getNextFacing());
        }
    }
}
